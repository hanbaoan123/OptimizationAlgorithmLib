package Util;

import java.awt.Color;

import tanling.matplot_4j.d3d.base.pub.Range;
import tanling.matplot_4j.d3d.base.pub.TopBottomColorStyle;
import tanling.matplot_4j.d3d.facade.Function;
import tanling.matplot_4j.d3d.facade.MatPlot3DMgr;

public class FunctionPointsDemo {

	public static void main(String[] args) {

		MatPlot3DMgr mgr = new MatPlot3DMgr();

		mgr.setDataInputType(MatPlot3DMgr.DATA_TYPE_FUNCTION3D);
		mgr.setShowType(MatPlot3DMgr.SHOW_TYPE_DOTS);//设置点阵列显示

		Function f = new Function() {

			public double f(double x, double y) {

				return Math.sin(y) * Math.cos(x) * 0.8;
			}

		};

		mgr.addData(f, new Range(-6, 6), new Range(-6, 6), 50, 50,new TopBottomColorStyle(Color.GREEN.darker().darker(),Color.YELLOW.brighter()));//自定义高低颜色，动态时每一帧高底色可以不同

		mgr.setScaleZ(2);
		mgr.setScaleX(1.3);
		mgr.setScaleY(1.3);
		
		mgr.setSeeta(0.78);
		mgr.setBeita(1.0);
		
		mgr.setTitle("Demo : 函数点阵   [  z =  0.8 * cos(x) * sin(y) ]");
		mgr.getProcessor().setCoordinateSysShowType(mgr.getProcessor().COORDINATE_SYS_ALWAYS_FURTHER);

		mgr.show();
		MatPlot3DMgr mgr2 = new MatPlot3DMgr();
		mgr2.show();
	}

}
