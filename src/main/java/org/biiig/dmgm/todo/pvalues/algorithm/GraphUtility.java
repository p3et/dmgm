package org.biiig.dmgm.todo.pvalues.algorithm;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.mutable.MutableDouble;
import org.biiig.dmgm.impl.operators.subgraph_mining.DFSCode;

import java.util.Arrays;
import java.util.Map;
import java.util.Vector;

public class GraphUtility {


  //private static double numAutos;
  public Occurrence toOccurrence(DFSCode dfsCode,
    Map<Integer, String> vertexDictionary, Map<Integer, String> edgeDictionary) {

    int numNodes = dfsCode.getVertexCount();

    String[] nodeLabels = new String[numNodes];

    String[][][] edgeLabels = new String[numNodes][numNodes][];

    nodeLabels[0] = vertexDictionary.get(dfsCode.getVertexLabel(0));
    for (int edgeTime = 0; edgeTime < dfsCode.getEdgeCount(); edgeTime++) {
      int sourceId;
      int targetId;

      boolean outgoing = dfsCode.isOutgoing(edgeTime);
      int toTime = dfsCode.getToTime(edgeTime);
      if (outgoing) {
        sourceId = dfsCode.getFromTime(edgeTime);
        targetId = toTime;
      } else {
        sourceId = toTime;
        targetId = dfsCode.getFromTime(edgeTime);
      }

      String edgeLabel = edgeDictionary.get(dfsCode.getEdgeLabel(edgeTime));
      edgeLabels[sourceId][targetId] = ArrayUtils.add(edgeLabels[sourceId][targetId], edgeLabel);
      nodeLabels[toTime] = vertexDictionary.get(dfsCode.getVertexLabel(toTime));
    }
    for (int i = 0; i < edgeLabels.length; i++) {
      for (int j = 0; j < edgeLabels[i].length; j++) {
        if (edgeLabels[i][j] != null) {
          Arrays.sort(edgeLabels[i][j]);
        }
      }
    }
    return new Occurrence(nodeLabels, edgeLabels);

  }

  public double findAutomorphisms(Occurrence occ) {
    int i, j, k;
    MutableDouble numAutos = new MutableDouble(0.0);
    int g;
    String[] nodeLabels = occ.getNodeLabels();
    int nof_nodes = nodeLabels.length;
    String[][][] edgeLabels = occ.getEdgeLabels();
    int[] fDir = new int[nof_nodes];
    int[] fRev = new int[nof_nodes];
    for (i = 0; i < nof_nodes; i++) {
      fDir[i] = -1;
      fRev[i] = -1;
    }
    int[][] sequence = new int[nof_nodes][nof_nodes];
    for (i = 0; i < nof_nodes; i++) {
      for (j = 0; j < nof_nodes; j++) {
        if (edgeLabels[i][j] != null || edgeLabels[j][i] != null) {
          int numNeighs = 0;
          for (k = 0; k < nof_nodes; k++) {
            if (edgeLabels[j][k] != null) {
              numNeighs++;
            }
            if (edgeLabels[k][j] != null && edgeLabels[j][k] == null) {
              numNeighs++;
            }
          }
          sequence[i][j] = numNeighs;
        } else {
          sequence[i][j] = 0;
        }
      }
    }
    for (i = 0; i < nof_nodes; i++)
      Arrays.sort(sequence[i]);

    boolean[] support = new boolean[nof_nodes * nof_nodes];
    for (i = 0; i < nof_nodes * nof_nodes; i++)
      support[i] = false;
    for (i = 0; i < nof_nodes; i++) {
      for (j = 0; j < nof_nodes; j++) {
        for (k = 0; k < nof_nodes; k++) {
          if (sequence[i][k] != sequence[j][k]) {
            break;
          }
        }
        if (k >= nof_nodes) {
          support[i * nof_nodes + j] = true;
        }
      }
    }
    for (g = 0; g < nof_nodes; g++) {
      if (support[g * nof_nodes]) {
        fDir[0] = g;
        fRev[g] = 0;
        int pos = 1;
        isomorphicExtensions(fDir, fRev, support, pos, nodeLabels, edgeLabels, numAutos);
        fRev[fDir[0]] = -1;
        fDir[0] = -1;
      }
    }
    return numAutos.doubleValue();
  }

  private void isomorphicExtensions(int[] fDir, int[] fRev, boolean[] support, int pos,
    String[] nodeLabels, String[][][] edgeLabels, MutableDouble numAutos) {
    int i, j;
    int nof_nodes = fDir.length;
    int[] cand = new int[nof_nodes];
    int ncand;
    int num;
    for (i = 0; i < nof_nodes; i++)
      cand[i] = -1;
    if (pos == nof_nodes) {
      numAutos.increment();
    } else {
      int n, m;
      boolean flag;
      int[] count = new int[nof_nodes];
      ncand = 0;
      for (i = 0; i < nof_nodes; i++)
        count[i] = 0;
      for (i = 0; i < nof_nodes; i++)     // For all nodes of H already mapped
      {
        if (fDir[i] != -1) {        // find their not mapped neighbours
          Vector<Integer> vNei = new Vector<>();
          for (j = 0; j < nof_nodes; j++) {
            if (edgeLabels[i][j] != null) {
              vNei.add(j);
            }
            if (edgeLabels[j][i] != null && edgeLabels[i][j] == null) {
              vNei.add(j);
            }
          }
          num = vNei.size();
          for (j = 0; j < num; j++) {
            int neigh = vNei.get(j);
            if (fDir[neigh] == -1) {
              if (count[neigh] == 0) {
                cand[ncand++] = neigh;
              }
              count[neigh]++;
            }
          }
        }
      }
      // Find most constrained neighbour 'm' (with more mapped neighbours)
      m = 0;
      for (i = 1; i < ncand; i++) {
        if (count[i] > count[m])  // Later: add more restraining conditions??
        {
          m = i;
        }
      }
      m = cand[m];
      ncand = 0;
      boolean[] already = new boolean[nof_nodes];
      for (i = 0; i < nof_nodes; i++)
        already[i] = false;
      for (i = 0; i < nof_nodes; i++)  // For all nodes of G already mapped
      {
        if (fDir[i] != -1) {         // find their not mapped neighbours
          Vector<Integer> vNei = new Vector<>();
          for (j = 0; j < nof_nodes; j++) {
            if (edgeLabels[fDir[i]][j] != null) {
              vNei.add(j);
            }
            if (edgeLabels[j][fDir[i]] != null && edgeLabels[fDir[i]][j] == null) {
              vNei.add(j);
            }
          }
          num = vNei.size();
          for (j = 0; j < num; j++) {
            int neigh = vNei.get(j);
            if (!already[neigh] && fRev[neigh] == -1 && support[m * nof_nodes + neigh]) {
              cand[ncand++] = neigh;
              already[neigh] = true;
            }
          }
        }
      }
      for (i = 0; i < ncand; i++) {
        n = cand[i];
        flag = false;
        if (nodeLabels[n].equals(nodeLabels[m])) {
          for (j = 0; j < nof_nodes; j++) {
            if (fDir[j] != -1) {
              if (!Arrays.equals(edgeLabels[m][j], edgeLabels[n][fDir[j]])) {
                flag = true;
                break;
              } else if (!Arrays.equals(edgeLabels[j][m], edgeLabels[fDir[j]][n])) {
                flag = true;
                break;
              }
            }
          }
          if (!flag) {
            fDir[m] = n;
            fRev[n] = m;
            pos++;
            isomorphicExtensions(fDir, fRev, support, pos, nodeLabels, edgeLabels, numAutos);
            pos--;
            fRev[fDir[m]] = -1;
            fDir[m] = -1;
          }
        }
      }
    }
  }

}