package org.biiig.dmgm.tfsm.dmg_gspan;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.time.StopWatch;
import org.biiig.dmgm.tfsm.dmg_gspan.impl.gspan.DFSCode;
import org.biiig.dmgm.tfsm.dmg_gspan.impl.mining.GenSpan;
import org.biiig.dmgm.tfsm.dmg_gspan.impl.model.countable.Countable;
import org.biiig.dmgm.tfsm.dmg_gspan.impl.model.multilevel_graph.MultiLevelVertex;
import org.biiig.dmgm.tfsm.dmg_gspan.impl.pvalues.CalculatePValue;
import org.biiig.dmgm.tfsm.dmg_gspan.impl.pvalues.model.EdgeData;
import org.biiig.dmgm.tfsm.dmg_gspan.impl.pvalues.model.MultiEdgeLabelDistributionKey;
import org.biiig.dmgm.tfsm.dmg_gspan.impl.pvalues.model.VertexDegreeKey;
import org.biiig.dmgm.tfsm.dmg_gspan.impl.model.labeled_graph.LabeledEdge;
import org.biiig.dmgm.tfsm.dmg_gspan.impl.model.multilevel_graph.MultiLevelGraph;
import org.biiig.dmgm.tfsm.dmg_gspan.impl.vector_mining.CrossLevelFrequentVectorsTopDown;
import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class PValueBench {

  String inputPath = "/home/peet/gfsm_bench/100_0.tlf";
//  String inputPath = "C:/NetAnalysis/Motifs/GradoopSingle/target/classes/samples/predictable.tlf";
//  String inputPath = GenSpan.class.getResource("/samples/predictable.tlf").getFile();

  @Test
  public void mineBaseLine() throws IOException {
    for (float threshold : new float[] {0.5f}) {
      for (int kmax : new int[] {10}) {
        System.out.println("start mining");

        GenSpan
          gSpan = new GenSpan(inputPath, threshold, new CrossLevelFrequentVectorsTopDown(), kmax);
        gSpan.mine();

        System.out.println("start statistics");

        List<MultiLevelGraph> graphs = gSpan.getGraphs();
        Map<Integer, String> vertexDictionary = gSpan.getReverseVertexDictionary();
        Map<Integer, String> edgeDictionary = gSpan.getReverseVertexDictionary();
        edgeDictionary.put(Integer.MAX_VALUE, "isA");

        Long globalVertexCount = 0L;

        List<EdgeData> edgeData = Lists.newArrayList();

        for (MultiLevelGraph graph : graphs) {
          globalVertexCount += graph.getVertexCount();

          for (LabeledEdge edge : graph.getEdges()) {
            //System.out.println(edge.getTopLevelLabel()+" --> "+edgeDictionary.get(edge.getTopLevelLabel()));

            for (int sourceLabel : graph.getVertices()[edge.getSource()].getAllLabels()) {
              for (int targetLabel : graph.getVertices()[edge.getTarget()].getAllLabels()) {
                edgeData.add(new EdgeData(
                  graph.getId(),
                  edge.getSource(),
                  edge.getTarget(),
                  vertexDictionary.get(sourceLabel),
                  edgeDictionary.get(edge.getLabel()),
                  vertexDictionary.get(targetLabel)));
              }
            }
          }
        }

        // group by source and target and compute max multiplicity
        int maxMultiplicity=0;
        edgeData.sort(EdgeData::compareTo);
        Iterator<EdgeData> iterator = edgeData.iterator();
        EdgeData last = iterator.next();
        while (iterator.hasNext()) {
          EdgeData next = iterator.next();

          if (last.equals(next)) {
            //aggregate multi-edge labels
            last.setEdgeLabels(ArrayUtils.addAll(last.getEdgeLabels(), next.getEdgeLabels()));
            iterator.remove();
          } else {
            Arrays.sort(last.getEdgeLabels());
            int multiplicity=last.getEdgeLabels().length;
            //System.out.println("multi "+multiplicity);
            if(multiplicity>maxMultiplicity)
              maxMultiplicity=multiplicity;
            last = next;
          }
        }

        //Compute vertex out- and in-degrees with respect to different multiplicities
        Map<Integer,long[]> mapOutDegrees=new HashMap<>();
        Map<Integer,long[]> mapInDegrees=new HashMap<>();
        for(EdgeData edge: edgeData)
        {
          int idSource=edge.getSourceId();
          int idTarget=edge.getTargetId();
          int multiplicity=edge.getEdgeLabels().length;
          if(mapOutDegrees.containsKey(idSource))
            mapOutDegrees.get(idSource)[multiplicity]++;
          else
          {
            long[] outDegs=new long[maxMultiplicity+1];
            outDegs[multiplicity]++;
            mapOutDegrees.put(idSource,outDegs);
          }
          if(mapInDegrees.containsKey(idTarget))
            mapInDegrees.get(idTarget)[multiplicity]++;
          else
          {
            long[] inDegs=new long[maxMultiplicity+1];
            inDegs[multiplicity]++;
            mapInDegrees.put(idTarget,inDegs);
          }
        }

        //Compute vertex degree distribution
        Map<VertexDegreeKey, Map<Long, Float>> vertexDegreeDistribution=new HashMap<>();
        for (EdgeData edge : edgeData) {
          int multiplicity=edge.getMultiplicity();
          long sourceOutDeg=mapOutDegrees.get(edge.getSourceId())[multiplicity];
          long targetInDeg=mapInDegrees.get(edge.getTargetId())[multiplicity];
          //System.out.println(multiplicity + " key "+edge.getSourceLabel());
          //System.out.println(multiplicity + " key "+edge.getTargetLabel());
          VertexDegreeKey keyOut = new VertexDegreeKey(true, multiplicity, edge.getSourceLabel());
          VertexDegreeKey keyIn = new VertexDegreeKey(false, multiplicity, edge.getTargetLabel());
          Map<Long,Float> mapOutDegs=vertexDegreeDistribution.get(keyOut);
          if(mapOutDegs==null)
          {
            mapOutDegs=new HashMap<>();
            mapOutDegs.put(sourceOutDeg,1.0f);
            vertexDegreeDistribution.put(keyOut,mapOutDegs);
          }
          else
          {
            if(mapOutDegs.containsKey(sourceOutDeg))
              mapOutDegs.put(sourceOutDeg,mapOutDegs.get(sourceOutDeg)+1);
            else
              mapOutDegs.put(sourceOutDeg,1.0f);
          }
          Map<Long,Float> mapInDegs=vertexDegreeDistribution.get(keyIn);
          if(mapInDegs==null)
          {
            mapInDegs=new HashMap<>();
            mapInDegs.put(targetInDeg,1.0f);
            vertexDegreeDistribution.put(keyIn,mapInDegs);
          }
          else
          {
            if(mapInDegs.containsKey(targetInDeg))
              mapInDegs.put(targetInDeg,mapInDegs.get(targetInDeg)+1);
            else
              mapInDegs.put(targetInDeg,1.0f);
          }
        }

        //Compute vertex label distribution
        Map<String, Float> vertexLabelDistribution=new HashMap<>();
        for (MultiLevelGraph graph : graphs)
        {
          for (MultiLevelVertex vertex : graph.getVertices())
          {
            for (int vertexLab : vertex.getAllLabels()) {
              String vertexStringLab=vertexDictionary.get(vertexLab);
              if(vertexLabelDistribution.containsKey(vertexStringLab))
                vertexLabelDistribution.put(vertexStringLab,vertexLabelDistribution.get(vertexStringLab)+1);
              else
                vertexLabelDistribution.put(vertexStringLab,1.0f);
            }
          }
        }

        //Compute edge label distributions conditioned on source and target labels
        Map<MultiEdgeLabelDistributionKey, Float> edgeLabelDistribution=new HashMap<>();
        Map<String,Integer> mapEdgeCounts=new HashMap<>();
        for(EdgeData edge : edgeData)
        {
          String sourceLab=edge.getSourceLabel();
          String targetLab=edge.getTargetLabel();
          String[] edgeLabs=edge.getEdgeLabels();
          //System.out.println(edgeLabs[0]);
          StringBuilder builder=new StringBuilder(edgeLabs[0]);
          for(int k=1;k<edgeLabs.length;k++)
          {
            builder.append(",");
            builder.append(edgeLabs[k]);
          }
          String multiEdgeLab=builder.toString();
          MultiEdgeLabelDistributionKey multiKey=null;
          String edgeKey=null;
          if(sourceLab.compareTo(targetLab)<=0)
          {
            multiKey = new MultiEdgeLabelDistributionKey(sourceLab, targetLab, multiEdgeLab);
            edgeKey=sourceLab+"-"+targetLab;
          }
          else
          {
            multiKey = new MultiEdgeLabelDistributionKey(targetLab, sourceLab, multiEdgeLab);
            edgeKey=targetLab+"-"+sourceLab;
          }
          if(edgeLabelDistribution.containsKey(multiKey))
            edgeLabelDistribution.put(multiKey,edgeLabelDistribution.get(multiKey)+1);
          else
            edgeLabelDistribution.put(multiKey,1.0f);
          if(mapEdgeCounts.containsKey(edgeKey))
            mapEdgeCounts.put(edgeKey,mapEdgeCounts.get(edgeKey)+1);
          else
            mapEdgeCounts.put(edgeKey,1);
        }
        for(MultiEdgeLabelDistributionKey multiKey: edgeLabelDistribution.keySet())
        {
          String sourceLab=multiKey.getSmallerLabel();
          String targetLab=multiKey.getGreaterLabel();
          String edgeKey=sourceLab+"-"+targetLab;
          edgeLabelDistribution.put(multiKey,edgeLabelDistribution.get(multiKey)/mapEdgeCounts.get(edgeKey));
        }

        CalculatePValue calculatePValue = new CalculatePValue(
          vertexDegreeDistribution,
          edgeLabelDistribution,
          vertexLabelDistribution,
          globalVertexCount,
          maxMultiplicity,
          vertexDictionary,
          edgeDictionary
        );

        System.out.println("start pValueCalculation");




        Map<Integer, List<Countable<DFSCode>>> map = Maps.newHashMap();

        for (Countable<DFSCode> code : gSpan.getResult()) {
          int size = code.getObject().size();

          List<Countable<DFSCode>> siblings = map.get(size);
          if (siblings == null) {
            map.put(size, Lists.newArrayList(code));
          } else {
            if (siblings.size() < 1000 ) {
              siblings.add(code);
            }
          }
        }

        for (Map.Entry<Integer, List<Countable<DFSCode>>> entry : map.entrySet()) {
          int size = entry.getKey();
          List<Countable<DFSCode>> codes = entry.getValue();

          StopWatch watch = new StopWatch();
          watch.start();

          codes.forEach(c -> calculatePValue.calculate(c));

          watch.stop();

          System.out.println(size + "|" + watch.getTime() + "|" + codes.size());
        }

      }
    }
  }

}