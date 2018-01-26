package org.biiig.dmgm.todo.pvalues;

import org.biiig.dmgm.impl.graph.DFSCode;
import org.biiig.dmgm.todo.pvalues.algorithm.*;
import org.biiig.dmgm.todo.pvalues.model.MultiEdgeLabelDistributionKey;
import org.biiig.dmgm.todo.pvalues.model.VertexDegreeKey;

import java.util.Map;

public class CalculatePValue {

  private final Map<VertexDegreeKey, Map<Long, Float>> vertexDegreeDistribution;
  private final Map<MultiEdgeLabelDistributionKey, Float> edgeLabelDistribution;
  private final Map<String, Float> vertexLabelDistribution;
  private final Long globalVertexCount;
  private final Integer maxMultiplicity;
  private final GraphUtility gu = new GraphUtility();
  private final Map<Integer, String> vertexDictionary;
  private final Map<Integer, String> edgeDictionary;


  public CalculatePValue(Map<VertexDegreeKey, Map<Long, Float>> vertexDegreeDistribution,
    Map<MultiEdgeLabelDistributionKey, Float> edgeLabelDistribution,
    Map<String, Float> vertexLabelDistribution, Long globalVertexCount, Integer maxMultiplicity,
    Map<Integer, String> vertexDictionary, Map<Integer, String> edgeDictionary) {
    this.vertexDegreeDistribution = vertexDegreeDistribution;
    this.edgeLabelDistribution = edgeLabelDistribution;
    this.vertexLabelDistribution = vertexLabelDistribution;
    this.globalVertexCount = globalVertexCount;
    this.maxMultiplicity = maxMultiplicity;
    this.vertexDictionary = vertexDictionary;
    this.edgeDictionary = edgeDictionary;
  }

  public double calculate(DFSCode countable) {

    long numberOfOccurrences = 0l; //countable.getFrequency();
    Occurrence occurrence = null;// gu.toOccurrence(countable.getObject(), db, edgeDictionary);
    int vertexCount = occurrence.getNodeLabels().length;

    EDDModel edd = new EDDModel(
      vertexDegreeDistribution,
      edgeLabelDistribution,
      vertexLabelDistribution,
      this.globalVertexCount,
      maxMultiplicity,
      vertexCount
    );

    PolyaAeppli pa = new PolyaAeppli();
    double pValue;
    double numAutos = gu.findAutomorphisms(occurrence);
    double numNRP = MathUtility.factorial(vertexCount) / numAutos;
    double meanAnal = edd.computeMean(occurrence, numNRP);
    /*System.out.println("Num of occs: "+numberOfOccurrences);
    System.out.println("Mean anal: "+meanAnal);*/
    pValue = pa.estimateMaxPvalue(numberOfOccurrences, meanAnal);

    if (pValue < -1.0E-17) {
      pValue = 0.0;
    } else if (pValue > 1.0) {
      pValue = 1.0;
    }
    //System.out.println("MotifSize: "+vertexCount+"\tfactorial: "+MathUtility.factorial(vertexCount)
    // +"\tnumAutos: "+numAutos+"\tnrp: "+numNRP+"\tPVALUE: "+pValue);

//    System.out.println("PVALUE: " + pValue);

    return pValue;
  }
}
