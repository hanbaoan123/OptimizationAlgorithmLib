package Util;

import de.dislin.Dislin;

public class curve {
	public static void main(String args[]) {
		int n = 100, i, ic;
		double x, fpi = 3.1415926 / 180., step = 360. / (n - 1);
		float xray[] = new float[n];
		float y1ray[] = new float[n];
		float y2ray[] = new float[n];
		for (i = 0; i < n; i++) {
			xray[i] = (float) (i * step);
			x = xray[i] * fpi;
			y1ray[i] = (float) Math.sin(x);
			y2ray[i] = (float) Math.cos(x);
		}
		Dislin.metafl("cons");
		Dislin.disini();
		Dislin.pagera();
		Dislin.complx();
		Dislin.axspos(450, 1800);
		Dislin.axslen(2200, 1200);
		Dislin.name("X-axis", "x");
		Dislin.name("Y-axis", "y");
		Dislin.labdig(-1, "x");
		Dislin.ticks(10, "xy");
		Dislin.titlin("Demonstration of CURVE", 1);
		Dislin.titlin("SIN(X), COS(X)", 3);
		ic = Dislin.intrgb(0.95f, 0.95f, 0.95f);
		Dislin.axsbgd(ic);
		Dislin.graf(0.f, 360.f, 0.f, 90.f, -1.f, 1.f, -1.f, 0.5f);
		Dislin.setrgb(0.7f, 0.7f, 0.7f);
		Dislin.grid(1, 1);
		Dislin.color("fore");
		Dislin.box2d();
		Dislin.height(50);
		Dislin.title();
		Dislin.color("red");
		Dislin.curve(xray, y1ray, n);
		Dislin.color("green");
		Dislin.curve(xray, y2ray, n);
		Dislin.disfin();
	}
}
