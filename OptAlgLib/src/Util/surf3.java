package Util;

import de.dislin.Dislin;

public class surf3 {
	  public static void main (String args []) {
	     int n = 50, m = 50, i, j, nlev = 20;
	     float zmat []  = new float [n*m];
	     float xray []  = new float [n];
	     float yray []  = new float [m];
	     float zlev []  = new float [nlev];

	     double x, y, step;
	     double fpi = 3.1415926/180.;
	     double stepx = 360. / (n-1);
	     double stepy = 360. / (m-1);
	     for (i = 0; i < n; i++) {
	       x = i * stepx;
	       xray[i] = (float) x;
	       for (j = 0; j < m; j++) {
	         y = j * stepy;
	         yray[j] = (float) y;
	         zmat[i*m+j] = (float) (2 * Math.sin(x*fpi)* Math.sin(y*fpi));
	       }
	     }
	       
	     Dislin.scrmod ("revers");
	     Dislin.metafl ("cons");
	     Dislin.setpag ("da4p");
	     Dislin.disini ();
	     Dislin.pagera ();
	     Dislin.hwfont ();

	     Dislin.axspos (200, 2600);
	     Dislin.axslen (1800, 1800);
	         
	     Dislin.name   ("X-axis", "x");
	     Dislin.name   ("Y-axis",  "y");
	     Dislin.name   ("Z-axis",  "z");

	     Dislin.titlin ("Shaded Surface / Contour Plot", 1);
	     Dislin.titlin ("F(X,Y) = 2*SIN(X)*SIN(Y)", 3);

	     Dislin.graf3d (0.f, 360.f, 0.f, 90.f,
	                    0.f, 360.f, 0.f, 90.f,
	                    -2.f, 2.f, -2.f, 1.f);
	     Dislin.height (50);
	     Dislin.title  ();
	 
	     Dislin.grfini (-1.f, -1.f, -1.f, 1.f, -1.f, -1.f, 1.f, 1.f, -1.f);
	     Dislin.nograf ();
	     Dislin.graf (0.f, 360.f, 0.f, 90.f, 0.f, 360.f, 0.f, 90.f);
	     step = 4. / nlev;
	     for (i = 0; i < nlev; i++)
	       zlev[i] = (float) (-2.0 + i * step); 

	     Dislin.conshd (xray, n, yray, n, zmat, zlev, nlev);
	     Dislin.box2d ();
	     Dislin.reset ("nograf");
	     Dislin.grffin ();

	     Dislin.shdmod  ("smooth", "surface");
	     Dislin.surshd (xray, n, yray, m, zmat);
	     Dislin.disfin ();
	  }
	}