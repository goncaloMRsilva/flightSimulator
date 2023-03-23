package miniprojeto2d;

import java.awt.*;
import java.awt.geom.*;

public class Cloud implements Shape {
	GeneralPath path;
	
	public Cloud(float x, float y, float w, float h) {
		path = new GeneralPath();
		float x0 = x + 0.25f * w;
		float y0 = y + 0.25f * h;
		float x1 = x + 0.25f * w;
		float y1 = y + 0.75f * h;
		float x2 = y + 0.5f * w;
		float y2 = y + 0.75f * h;
		float x3 = x + 0.75f * w;
		float y3 = y + 0.75f * h;
		float x4 = x + 0.75f * w;
		float y4 = y + 0.25f * h;
		float x5 = x + 0.5f * w;
		float y5 = y + 0.25f * h;
		float x6 = x;
		float y6 = y + 0.5f * h;
		float x7 = x + 0.5f * w;
		float y7 = y + h;
		float x8 = x + 0.625f * w;
		float y8 = y + h;
		float x9 = x + w;
		float y9 = y + 0.5f * h;
		float x10 = x + 0.625f * w;
		float y10 = y;
		float x11 = x + 0.375f * w;
		float y11 = y;

		
		path.moveTo(x0,y0);
		path.quadTo(x6, y6, x1, y1);
		path.quadTo(x7, y7, x2, y2);
		path.quadTo(x7, y7, x2, y2);
		path.quadTo(x8, y8, x3, y3);
		path.quadTo(x9, y9, x4, y4);
		path.quadTo(x10, y10, x5, y5);
		path.quadTo(x11, y11, x0, y0);
	}

	/*
	@Override
	public Rectangle getBounds() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Rectangle2D getBounds2D() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean contains(double x, double y) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean contains(Point2D p) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean intersects(double x, double y, double w, double h) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean intersects(Rectangle2D r) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean contains(double x, double y, double w, double h) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean contains(Rectangle2D r) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public PathIterator getPathIterator(AffineTransform at) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PathIterator getPathIterator(AffineTransform at, double flatness) {
		// TODO Auto-generated method stub
		return null;
	}
	*/
	
	public boolean contains(Rectangle2D rect) {
	    return path.contains(rect);
		 //return false;
	  }

	  public boolean contains(Point2D point) {
	    return path.contains(point);
	  }

	  public boolean contains(double x, double y) {
	    return path.contains(x, y);
	  }

	  public boolean contains(double x, double y, double w, double h) {
	    return path.contains(x, y, w, h);
	  }

	  public Rectangle getBounds() {
	    return path.getBounds();
	  }

	  public Rectangle2D getBounds2D() {
	    return path.getBounds2D();
	  }

	  public PathIterator getPathIterator(AffineTransform at) {
	    return path.getPathIterator(at);
	  }

	  public PathIterator getPathIterator(AffineTransform at, double flatness) {
	    return path.getPathIterator(at, flatness);
	  }

	  public boolean intersects(Rectangle2D rect) {
	    return path.intersects(rect);
	  }

	  public boolean intersects(double x, double y, double w, double h) {
	    return path.intersects(x, y, w, h);
	  }
}
