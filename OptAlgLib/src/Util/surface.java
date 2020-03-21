package Util;

import de.dislin.Dislin;

public class surface {
	public static void main(String args[]) {
		int n = 50, m = 50, i, j;
		float zmat[] = new float[n * m];
		float xray[] = new float[n];
		float yray[] = new float[m];

		double x, y;
		double fpi = 3.1415926 / 180.;
		double stepx = 360. / (n - 1);
		double stepy = 360. / (m - 1);
		for (i = 0; i < n; i++) {
			x = i * stepx;
			xray[i] = (float) x;
			for (j = 0; j < m; j++) {
				y = j * stepy;
				yray[j] = (float) y;
				zmat[i * m + j] = (float) (2 * Math.sin(x * fpi) * Math.sin(y * fpi));
			}
		}

		Dislin.scrmod("revers");
		Dislin.metafl("cons");
		Dislin.setpag("da4p");
		Dislin.disini();
		Dislin.pagera();
		Dislin.complx();

		Dislin.axspos(200, 2600);
		Dislin.axslen(1800, 1800);

		Dislin.name("X-axis", "x");
		Dislin.name("Y-axis", "y");
		Dislin.name("Z-axis", "z");

		Dislin.titlin("Shaded Surface Plot", 1);
		Dislin.titlin("F(X,Y) = 2*SIN(X)*SIN(Y)", 3);

		Dislin.view3d(-5.f, -5.f, 4.f, "abs");
		Dislin.graf3d(0.f, 360.f, 0.f, 90.f, 0.f, 360.f, 0.f, 90.f, -3.f, 3.f, -3.f, 1.f);
		Dislin.height(50);
		Dislin.title();
		Dislin.bmpmod(1, "", "");
		Dislin.shdmod("smooth", "surface");
		Dislin.surshd(xray, n, yray, m, zmat);
		Dislin.disfin();
	}
}