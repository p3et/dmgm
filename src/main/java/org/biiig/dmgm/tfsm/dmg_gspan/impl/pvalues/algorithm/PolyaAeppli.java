package org.biiig.dmgm.tfsm.dmg_gspan.impl.pvalues.algorithm;

public class PolyaAeppli
{
	private double computePValue(long numOccurrences, double mean, double variance)
	{
		double a=(variance-mean)/(mean+variance);
		double lambda=(1-a)*mean;
		double z=(1.0-a)*lambda/a;
		double res;
		if(numOccurrences==0)
			res=Math.exp(-lambda);
		else if(numOccurrences==1)
			res=Math.exp(-lambda)*(1.0+a*z);
		else
		{
			double precVal=Double.MIN_VALUE;
			double currVal=0.0;
			double Lprec=-lambda;
			double Lcour=Lprec+Math.log(a*z);
			double Acour=Lprec;
			double Scour=1.0+a*z;
			for (int i=2;i<=numOccurrences;i++)
			{
				double Lsuiv=Lcour+Math.log(a*(2.0*i-2.0+z)/i+a*a*(2.0-i)*Math.exp(Lprec-Lcour)/i);
				double Ssuiv=Scour+Math.exp(Lsuiv-Acour);
				double Asuiv;
				if(Ssuiv>0 && Ssuiv<Double.MAX_VALUE)
					Asuiv=Acour;
				else
				{
					Asuiv=Acour+Math.log(Scour);
					Ssuiv=1.0+Math.exp(Lsuiv-Asuiv);
				}
				Lprec=Lcour;
				Lcour=Lsuiv;
				Scour=Ssuiv;
				Acour=Asuiv;
				currVal=Scour*Math.exp(Acour);
				if(currVal>=1.0)
					break;
				if(currVal>1.0E-322 && currVal==precVal)
					break;
				else
					precVal=currVal;
			}
			res=currVal;
		}
		return 1.0-res;
	}

	public double estimateMaxPvalue(long numOccurrences,double mean)
	{
		double gr=(Math.sqrt(5)+1)/2;
		double minInter=0;
		double maxInter=Math.pow(mean+1,2);
		double tol=0.001;
		double c=maxInter-(maxInter-minInter)/gr;
		double d=minInter+(maxInter-minInter)/gr;
		while(Math.abs(c-d)>tol)
		{
			double fc=computePValue(numOccurrences,mean,c);
			double fd=computePValue(numOccurrences,mean,d);
			if(Double.isNaN(fc))
				fc=1.0;
			if(Double.isNaN(fd))
				fd=1.0;
			if(fc>fd)
				maxInter=d;
			else
				minInter=c;
			if(fc==1.0 && fd==1.0)
				break;
			c=maxInter-(maxInter-minInter)/gr;
			d=minInter+(maxInter-minInter)/gr;
		}
		double maxPval=computePValue(numOccurrences,mean,(maxInter+minInter)/2);
		if(Double.isNaN(maxPval))
			maxPval=1.0;
		return maxPval;
	}

}