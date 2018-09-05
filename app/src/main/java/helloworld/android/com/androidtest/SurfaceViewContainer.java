package helloworld.android.com.androidtest;

import android.graphics.Canvas;

import java.util.ArrayList;
import java.util.List;

public class SurfaceViewContainer {

    private List<SurfaceViewContainer> children;

    public SurfaceViewContainer(){
        children = new ArrayList<>();
    }

    public void draw(Canvas canvas){
        childrenView(canvas);

        for (SurfaceViewContainer child : children){
            child.draw(canvas);
        }
    }

    public void childrenView(Canvas canvas){};

    public void addChildrenView(SurfaceViewContainer child){
        children.add(child);
    }

    public void removeChildrenView(SurfaceViewContainer child){
        children.remove(child);
    }
}
