package org.biiig.dmgm.tfsm.dmg_gspan.pvalues.algorithm;

public class MathUtility
{
	private static int matBinomCoeff[][]={
	{1,  0,  0,   0,   0,   0,   0,   0,  0,  0, 0} ,
	{1,  1,  0,   0,   0,   0,   0,   0,  0,  0, 0} ,
	{1,  2,  1,   0,   0,   0,   0,   0,  0,  0, 0} ,
	{1,  3,  3,   1,   0,   0,   0,   0,  0,  0, 0} ,
	{1,  4,  6,   4,   1,   0,   0,   0,  0,  0, 0} ,
	{1,  5, 10,  10,   5,   1,   0,   0,  0,  0, 0} ,
	{1,  6, 15,  20,  15,   6,   1,   0,  0,  0, 0} ,
	{1,  7, 21,  35,  35,  21,   7,   1,  0,  0, 0} ,
	{1,  8, 28,  56,  70,  56,  28,   8,  1,  0, 0} ,
	{1,  9, 36,  84, 126, 126,  84,  36,  9,  1, 0} ,
	{1, 10, 45, 120, 210, 252, 210, 120, 45, 10, 1}
	};
	public static double binomCoeff(long n, long k)
	{
		if(k>n)
			return 0;
		else
		{
			if(k==0)
				return 1;
			else if(k==1)
				return n;
			else if(n<=10) {
				return matBinomCoeff[(int)n][(int)k];
			}
			else
			{
				//Vandermonde identity
				if(k>(n/2))
					k=n-k;
				long m1=n/2;
				long m2=n-m1;
				double res=0;
				if(m1!=m2)
					// Pascal triangle
					res=binomCoeff(2*m1,k)+binomCoeff(2*m1,k-1);
				else
				{
					long k_end ;
					if(k%2==1)
						//Odd
						k_end=(k+1)/2;
					else
					{
						//even
						k_end=k/2;
						res=binomCoeff(m1,k_end );
						res=res*res;
					}
					for(int i=0;i<k_end;i++)
						res+=2*(binomCoeff(m1,i)*binomCoeff(m1,k-i));
				}
				return res;
			}
		}
	}

	public static double factorial(long n)
	{
		double res = 1.0;
		for(long i=2;i<=n;i++)
			res*= i;
		return res;
	}

}
	