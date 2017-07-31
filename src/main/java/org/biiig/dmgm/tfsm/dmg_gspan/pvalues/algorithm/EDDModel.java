package org.biiig.dmgm.tfsm.dmg_gspan.pvalues.algorithm;

import org.biiig.dmgm.tfsm.dmg_gspan.pvalues.model.MultiEdgeLabelDistributionKey;
import org.biiig.dmgm.tfsm.dmg_gspan.pvalues.model.VertexDegreeKey;

import java.util.HashMap;
import java.util.Map;

public class EDDModel {
  private Map<VertexDegreeKey, Map<Long, Float>> vertexDegreeDistribution;
  private Map<MultiEdgeLabelDistributionKey, Float> edgeLabelDistribution;
  private Map<String, Float> vertexLabelDistribution;
  private long vertexCount;
  private int maxMultiplicity;
  private double[] gamma;
  private boolean directed;
  private HashMap<String, double[]>[] momentsOutDep;
  private HashMap<String, double[]>[] momentsInDep;

  public EDDModel(Map<VertexDegreeKey, Map<Long, Float>> vertexDegreeDistribution,
    Map<MultiEdgeLabelDistributionKey, Float> edgeLabelDistribution,
    Map<String, Float> vertexLabelDistribution, Long vertexCount, Integer maxMultiplicity,
    int motifSize) {
    this.vertexDegreeDistribution = vertexDegreeDistribution;
    this.edgeLabelDistribution = edgeLabelDistribution;
    this.vertexLabelDistribution = vertexLabelDistribution;
    //System.out.println(vertexCount);
    this.vertexCount = vertexCount;
    this.maxMultiplicity = maxMultiplicity;
    this.directed = true;
    this.gamma = new double[maxMultiplicity + 1];
    computeGamma();
		/*if(motifSize<=maxExact)
			setMoments(2*motifSize);
		else*/
    setMoments(motifSize);
  }

  private void computeGamma() {
    int i;
    for (i = 1; i <= maxMultiplicity; i++) {
      double meanDegree = 0.0;
      for (String nodeLab : vertexLabelDistribution.keySet()) {
        VertexDegreeKey key = new VertexDegreeKey(true, i, nodeLab);
        if (vertexDegreeDistribution.containsKey(key)) {
          Map<Long, Float> mapDegs = vertexDegreeDistribution.get(key);
          for (long deg : mapDegs.keySet()) {
            double valDeg = mapDegs.get(deg) / vertexLabelDistribution.get(nodeLab);
            double freqLab = vertexLabelDistribution.get(nodeLab) / vertexCount;
            meanDegree += valDeg * freqLab * deg;
          }
        }
      }
      if (meanDegree != 0.0)
        gamma[i] = 1.0 / ((vertexCount - 1) * meanDegree);
    }
  }

  public double computeMean(Occurrence occ, double numNRP) {
    double probOcc = computeOccProb(occ);
    double binomCoeff = MathUtility.binomCoeff(vertexCount, occ.getNodeLabels().length);
		/*System.out.println("binomCoeff: "+binomCoeff);
		System.out.println("probOcc: "+probOcc);
		System.out.println("numNonRedPerm: "+numNRP);*/
    return binomCoeff * numNRP * probOcc;
  }

  private double computeOccProb(Occurrence occ) {
    String[] nodeColors = occ.getNodeLabels();
    String[][][] edgeColors = occ.getEdgeLabels();
    int i, j, k;
    double probTopology = 1.0;
    int[][] degreeOccOut = new int[nodeColors.length][gamma.length];
    int[][] degreeOccIn = new int[nodeColors.length][gamma.length];
    if (directed) {
      for (i = 0; i < nodeColors.length; i++) {
        for (j = 0; j < nodeColors.length; j++) {
          if (edgeColors[i][j] != null) {
            StringBuilder builder = new StringBuilder(edgeColors[i][j][0]);
            for (k = 1; k < edgeColors[i][j].length; k++) {
              builder.append(",");
              builder.append(edgeColors[i][j][k]);
            }
            String multiEdgeLab = builder.toString();
            //System.out.println(nodeColors[i]+"-"+nodeColors[j]+"-"+multiEdgeLab);
            int multiplicity = Math.min(edgeColors[i][j].length, maxMultiplicity);
            degreeOccOut[i][multiplicity]++;
            degreeOccIn[j][multiplicity]++;
            MultiEdgeLabelDistributionKey key =
              MultiEdgeLabelDistributionKey.create(nodeColors[i], nodeColors[j], multiEdgeLab);
            if (edgeLabelDistribution.containsKey(key)) {
              probTopology *= edgeLabelDistribution.get(key);
            } else {
              //System.out.println("zero");
              probTopology *= 0.0;
            }
            probTopology *= gamma[multiplicity];
          }
        }
      }
    } else {
      for (i = 0; i < nodeColors.length; i++) {
        for (j = i + 1; j < nodeColors.length; j++) {
          if (edgeColors[i][j] != null) {
            StringBuilder builder = new StringBuilder(edgeColors[i][j][0]);
            for (k = 1; k < edgeColors[i][j].length; k++) {
              builder.append(",");
              builder.append(edgeColors[i][j][k]);
            }
            String multiEdgeLab = builder.toString();
            //System.out.println(nodeColors[i]+"-"+nodeColors[j]+"-"+multiEdgeLab);
            int multiplicity = Math.min(edgeColors[i][j].length, maxMultiplicity);
            degreeOccOut[i][multiplicity]++;
            degreeOccOut[j][multiplicity]++;
            MultiEdgeLabelDistributionKey key =
              MultiEdgeLabelDistributionKey.create(nodeColors[i], nodeColors[j], multiEdgeLab);
            if (edgeLabelDistribution.containsKey(key)) {
              probTopology *= edgeLabelDistribution.get(key);
            } else {
              //System.out.println("zero");
              probTopology *= 0.0;
            }
            probTopology *= gamma[multiplicity];
          }
        }
      }
    }
    for (i = 0; i < nodeColors.length; i++) {
      if (vertexLabelDistribution.containsKey(nodeColors[i])) {
        probTopology *= vertexLabelDistribution.get(nodeColors[i]) / vertexCount;
      } else {
        probTopology *= 0.0;
      }
      for (j = 1; j < momentsOutDep.length; j++) {
        double[] mom = momentsOutDep[j].get(nodeColors[i]);
        if (mom != null) {
          probTopology *= mom[degreeOccOut[i][j]];
        } else {
          probTopology *= 0.0;
        }
      }
      if (directed) {
        for (j = 1; j < momentsInDep.length; j++) {
          double[] mom = momentsInDep[j].get(nodeColors[i]);
          if (mom != null) {
            probTopology *= mom[degreeOccIn[i][j]];
          } else {
            probTopology *= 0.0;
          }
        }
      }
    }
    return probTopology;
  }

  private void setMoments(int degree) {
    momentsOutDep = new HashMap[gamma.length];
    momentsInDep = new HashMap[gamma.length];
    int i, k;
    for (i = 1; i < momentsOutDep.length; i++) {
      momentsOutDep[i] = new HashMap<>();
      momentsInDep[i] = new HashMap<>();
      for (String nodeLab : vertexLabelDistribution.keySet()) {
        VertexDegreeKey outKey = new VertexDegreeKey(true, i, nodeLab);
        VertexDegreeKey inKey = new VertexDegreeKey(false, i, nodeLab);
        double[] degArrayOut = new double[degree + 1];
        degArrayOut[0] = 1.0;
        double[] degArrayIn = new double[degree + 1];
        degArrayIn[0] = 1.0;
        for (k = 1; k < degArrayOut.length; k++) {
          degArrayOut[k] = 0.0;
          degArrayIn[k] = 0.0;
          if (vertexDegreeDistribution.containsKey(outKey)) {
            Map<Long, Float> mapDegsOut = vertexDegreeDistribution.get(outKey);
            for (long deg : mapDegsOut.keySet()) {
              double valDeg = mapDegsOut.get(deg) / vertexLabelDistribution.get(nodeLab);
              degArrayOut[k] += valDeg * Math.pow(deg, k);
            }
          }
          if (vertexDegreeDistribution.containsKey(inKey)) {
            Map<Long, Float> mapDegsIn = vertexDegreeDistribution.get(inKey);
            for (long deg : mapDegsIn.keySet()) {
              double valDeg = mapDegsIn.get(deg) / vertexLabelDistribution.get(nodeLab);
              degArrayIn[k] += valDeg * Math.pow(deg, k);
            }
          }
        }
        momentsOutDep[i].put(nodeLab, degArrayOut);
        momentsInDep[i].put(nodeLab, degArrayIn);
      }
    }
  }
}
	