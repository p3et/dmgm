//package org.biiig.dmgm.todo.dmg_gspan;
//
//import com.google.common.collect.Lists;
//import org.apache.commons.lang3.ArrayUtils;
//import org.biiig.dmgm.todo.mining.GenSpan;
//import org.biiig.dmgm.todo.mining.GenSpanBaseline;
//import org.biiig.dmgm.todo.model.labeled_graph.LabeledEdge;
//import org.biiig.dmgm.todo.model.labeled_graph.LabeledGraph;
//import org.biiig.dmgm.todo.pvalues.CalculatePValue;
//import org.biiig.dmgm.todo.pvalues.model.EdgeData;
//import org.biiig.dmgm.todo.pvalues.model.MultiEdgeLabelDistributionKey;
//import org.biiig.dmgm.todo.pvalues.model.VertexDegreeKey;
//import org.biiig.dmgm.todo.model.labeled_graph.LabeledVertex;
//import org.junit.Test;
//
//import java.source.IOException;
//import java.util.*;
//
//import static org.junit.Assert.assertEquals;
//
//public class PValueTest {
//
//  public static final float THRESHOLD = 0.8f;
//  public static final int K_MAX = 8;
//    String inputPath = GenSpan.class.getResource("/samples/multi_level.tlf").getFile();
//  //String inputPath = "C:/NetAnalysis/Motifs/GradoopSingle/target/classes/samples/predictable.tlf";
//  //String inputPath = "C:/NetAnalysis/Motifs/GradoopSingle/target/100_0.tlf";
//
//  @Test
//  public void mineBaseLine() throws IOException {
//    GenSpanBaseline gSpan = new GenSpanBaseline(inputPath, THRESHOLD, K_MAX);
//    gSpan.execute();
//
//    List<LabeledGraph> graphs = gSpan.getGraphs();
//    Map<Integer, String> vertexDictionary = gSpan.getReverseVertexDictionary();
//    Map<Integer, String> edgeDictionary = gSpan.getReverseEdgeDictionary();
//    edgeDictionary.put(Integer.MAX_VALUE, "isA");
//
//    Long globalVertexCount = 0L;
//
//    List<EdgeData> edgeData = Lists.newArrayList();
//
//    for (LabeledGraph graph : graphs) {
//      globalVertexCount += graph.getVertexCount();
//
//      for (LabeledEdge edge : graph.getEdges()) {
//        //System.out.println(edge.getTopLevelLabel()+" --> "+edgeDictionary.get(edge.getTopLevelLabel()));
//        edgeData.add(new EdgeData(
//          graph.getId(),
//          edge.getSource(),
//          edge.getTarget(),
//          vertexDictionary.get(graph.getVertices()[edge.getSource()].getLabel()),
//          edgeDictionary.get(edge.getLabel()),
//          vertexDictionary.get(graph.getVertices()[edge.getTarget()].getLabel())));
//      }
//    }
//
//    // group by source and target and compute max multiplicity
//    int maxMultiplicity=0;
//    edgeData.sort(EdgeData::compareTo);
//    Iterator<EdgeData> iterator = edgeData.iterator();
//    EdgeData last = iterator.next();
//    while (iterator.hasNext()) {
//      EdgeData next = iterator.next();
//
//      if (last.equals(next)) {
//        //aggregate multi-edge labels
//        last.setEdgeLabels(ArrayUtils.addAll(last.getEdgeLabels(), next.getEdgeLabels()));
//        iterator.remove();
//      } else {
//        Arrays.sort(last.getEdgeLabels());
//        int multiplicity=last.getEdgeLabels().length;
//        //System.out.println("multi "+multiplicity);
//        if(multiplicity>maxMultiplicity)
//          maxMultiplicity=multiplicity;
//        last = next;
//      }
//    }
//
//    //Compute vertex out- and in-degrees with respect to different multiplicities
//    Map<Integer,long[]> mapOutDegrees=new HashMap<>();
//    Map<Integer,long[]> mapInDegrees=new HashMap<>();
//    for(EdgeData edge: edgeData)
//    {
//      int idSource=edge.getSourceId();
//      int idTarget=edge.getTargetId();
//      int multiplicity=edge.getEdgeLabels().length;
//      if(mapOutDegrees.containsKey(idSource))
//        mapOutDegrees.get(idSource)[multiplicity]++;
//      else
//      {
//        long[] outDegs=new long[maxMultiplicity+1];
//        outDegs[multiplicity]++;
//        mapOutDegrees.put(idSource,outDegs);
//      }
//      if(mapInDegrees.containsKey(idTarget))
//        mapInDegrees.get(idTarget)[multiplicity]++;
//      else
//      {
//        long[] inDegs=new long[maxMultiplicity+1];
//        inDegs[multiplicity]++;
//        mapInDegrees.put(idTarget,inDegs);
//      }
//    }
//
//    //Compute vertex degree distribution
//    Map<VertexDegreeKey, Map<Long, Float>> vertexDegreeDistribution=new HashMap<>();
//    for (EdgeData edge : edgeData) {
//      int multiplicity=edge.getMultiplicity();
//      long sourceOutDeg=mapOutDegrees.get(edge.getSourceId())[multiplicity];
//      long targetInDeg=mapInDegrees.get(edge.getTargetId())[multiplicity];
//      //System.out.println(multiplicity + " key "+edge.getSourceLabel());
//      //System.out.println(multiplicity + " key "+edge.getTargetLabel());
//      VertexDegreeKey keyOut = new VertexDegreeKey(true, multiplicity, edge.getSourceLabel());
//      VertexDegreeKey keyIn = new VertexDegreeKey(false, multiplicity, edge.getTargetLabel());
//      Map<Long,Float> mapOutDegs=vertexDegreeDistribution.get(keyOut);
//      if(mapOutDegs==null)
//      {
//        mapOutDegs=new HashMap<>();
//        mapOutDegs.put(sourceOutDeg,1.0f);
//        vertexDegreeDistribution.put(keyOut,mapOutDegs);
//      }
//      else
//      {
//        if(mapOutDegs.containsKey(sourceOutDeg))
//          mapOutDegs.put(sourceOutDeg,mapOutDegs.get(sourceOutDeg)+1);
//        else
//          mapOutDegs.put(sourceOutDeg,1.0f);
//      }
//      Map<Long,Float> mapInDegs=vertexDegreeDistribution.get(keyIn);
//      if(mapInDegs==null)
//      {
//        mapInDegs=new HashMap<>();
//        mapInDegs.put(targetInDeg,1.0f);
//        vertexDegreeDistribution.put(keyIn,mapInDegs);
//      }
//      else
//      {
//        if(mapInDegs.containsKey(targetInDeg))
//          mapInDegs.put(targetInDeg,mapInDegs.get(targetInDeg)+1);
//        else
//          mapInDegs.put(targetInDeg,1.0f);
//      }
//    }
//
//    //Compute vertex format distribution
//    Map<String, Float> vertexLabelDistribution=new HashMap<>();
//    for (LabeledGraph graph : graphs)
//    {
//      for (LabeledVertex vertex : graph.getVertices())
//      {
//        int vertexLab=vertex.getLabel();
//        String vertexStringLab=vertexDictionary.get(vertexLab);
//        if(vertexLabelDistribution.containsKey(vertexStringLab))
//          vertexLabelDistribution.put(vertexStringLab,vertexLabelDistribution.get(vertexStringLab)+1);
//        else
//          vertexLabelDistribution.put(vertexStringLab,1.0f);
//      }
//    }
//
//    //Compute edge format distributions conditioned on source and target labels
//    Map<MultiEdgeLabelDistributionKey, Float> edgeLabelDistribution=new HashMap<>();
//    Map<String,Integer> mapEdgeCounts=new HashMap<>();
//    for(EdgeData edge : edgeData)
//    {
//      String sourceLab=edge.getSourceLabel();
//      String targetLab=edge.getTargetLabel();
//      String[] edgeLabs=edge.getEdgeLabels();
//      //System.out.println(edgeLabs[0]);
//      StringBuilder builder=new StringBuilder(edgeLabs[0]);
//      for(int k=1;k<edgeLabs.length;k++)
//      {
//        builder.append(",");
//        builder.append(edgeLabs[k]);
//      }
//      String multiEdgeLab=builder.toString();
//      MultiEdgeLabelDistributionKey multiKey=null;
//      String edgeKey=null;
//      if(sourceLab.compareTo(targetLab)<=0)
//      {
//        multiKey = new MultiEdgeLabelDistributionKey(sourceLab, targetLab, multiEdgeLab);
//        edgeKey=sourceLab+"-"+targetLab;
//      }
//      else
//      {
//        multiKey = new MultiEdgeLabelDistributionKey(targetLab, sourceLab, multiEdgeLab);
//        edgeKey=targetLab+"-"+sourceLab;
//      }
//      if(edgeLabelDistribution.containsKey(multiKey))
//        edgeLabelDistribution.put(multiKey,edgeLabelDistribution.get(multiKey)+1);
//      else
//        edgeLabelDistribution.put(multiKey,1.0f);
//      if(mapEdgeCounts.containsKey(edgeKey))
//        mapEdgeCounts.put(edgeKey,mapEdgeCounts.get(edgeKey)+1);
//      else
//        mapEdgeCounts.put(edgeKey,1);
//    }
//    for(MultiEdgeLabelDistributionKey multiKey: edgeLabelDistribution.keySet())
//    {
//      String sourceLab=multiKey.getSmallerLabel();
//      String targetLab=multiKey.getGreaterLabel();
//      String edgeKey=sourceLab+"-"+targetLab;
//      edgeLabelDistribution.put(multiKey,edgeLabelDistribution.get(multiKey)/mapEdgeCounts.get(edgeKey));
//    }
//
//    CalculatePValue calculatePValue = new CalculatePValue(
//      vertexDegreeDistribution,
//      edgeLabelDistribution,
//      vertexLabelDistribution,
//      globalVertexCount,
//      maxMultiplicity,
//      vertexDictionary,
//      edgeDictionary
//    );
//
//    gSpan.getResult().forEach(c -> calculatePValue.calculate(c));
//
//  }
//
//}