package org.biiig.dmgm.tfsm.dmg_gspan.pvalues.algorithm;

public class Occurrence
{
    private String[] nodeLabels;
    private String[][][] edgeLabels;
    public Occurrence(String[] nodeLabels, String[][][] edgeLabels)
    {
        this.nodeLabels=nodeLabels;
        this.edgeLabels=edgeLabels;
    }
    public String[] getNodeLabels()
    {
        return nodeLabels;
    }
    public String[][][] getEdgeLabels()
    {
        return edgeLabels;
    }

    public String toString(boolean directed)
    {
        int i, j, k;
        StringBuilder builder=new StringBuilder();
        for(j=0;j<nodeLabels.length-1;j++)
        {
            builder.append(nodeLabels[j]);
            builder.append(",");
        }
        builder.append(nodeLabels[j]);
        builder.append("\t");
        for(j=0;j<edgeLabels.length;j++)
        {
            for(k=0;k<edgeLabels[j].length;k++)
            {
                if((directed || j<k) && edgeLabels[j][k]!=null)
                {
                    builder.append("(");
                    builder.append(j);
                    builder.append(",");
                    builder.append(k);
                    for(i=0;i<edgeLabels[j][k].length;i++)
                    {
                        builder.append(",");
                        builder.append(edgeLabels[j][k][i]);
                    }
                    builder.append(")");
                }
            }
        }
        return builder.toString();
    }
}
