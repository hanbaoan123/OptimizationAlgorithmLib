package Util;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import tanling.matplot_4j.d3d.base.pub.Range;
import tanling.matplot_4j.d3d.facade.Function;
import tanling.matplot_4j.d3d.facade.MatPlot3DMgr;

public class SurfaceDemo {

	public static void main(String[] args) {

		MatPlot3DMgr mgr = new MatPlot3DMgr(true);

		mgr.setDataInputType(MatPlot3DMgr.DATA_TYPE_FUNCTION3D);

		Function f = new Function() {

			public double f(double x, double y) {

				// return Math.sin(y*x/2.2)*0.8;
				return Math.pow(Math.E, -Math.pow(x - 4, 2) - Math.pow(y - 4, 2))
						+ Math.pow(Math.E, -Math.pow(x + 4, 2) - Math.pow(y - 4, 2))
						+ 2 * Math.pow(Math.E, -Math.pow(x, 2) - Math.pow(y + 4, 2))
						+ 2 * Math.pow(Math.E, -Math.pow(x, 2) - Math.pow(y, 2));
			}

		};

		mgr.addData(f, new Range(-5, 5), new Range(-5, 5), 50, 50);

		mgr.setScaleZ(1.5);
		mgr.setScaleX(1.3);
		mgr.setScaleY(1.3);

		mgr.setSeeta(1.3);
		mgr.setBeita(1.1);

		mgr.setTitle("Demo : 函数曲面绘制   [ z =  0.8 * sin(y*x/2.2) ]");

		mgr.getProcessor().setCoordinateSysShowType(mgr.getProcessor().COORDINATE_SYS_ALWAYS_FURTHER);

//		mgr.show();

		// 可以将Panel对象加入到自己的框架中
		JFrame jf = new JFrame("InternalFrameTest");

		try {
			String lookAndFeel = UIManager.getSystemLookAndFeelClassName();
			UIManager.setLookAndFeel(lookAndFeel);
			SwingUtilities.updateComponentTreeUI(jf);
		} catch (Exception e) {
			e.printStackTrace();
		}

		jf.setContentPane(mgr.getPanel());

		jf.setDefaultCloseOperation(jf.EXIT_ON_CLOSE);
		jf.setSize(800, 800);
		jf.setVisible(true);
	}

}
