package com.zsj.lovelayout;

import android.animation.TypeEvaluator;
import android.graphics.PointF;

/**
 * @author 朱胜军
 */

public class BezierTypeEvaluator implements TypeEvaluator<PointF> {

    private PointF mPointF1;
    private PointF mPointF2;

    public BezierTypeEvaluator(PointF pointF1, PointF pointF2) {
        mPointF1 = pointF1;
        mPointF2 = pointF2;
    }

    @Override
    public PointF evaluate(float t, PointF point0, PointF point3) {
        PointF pointF = new PointF();
        pointF.x = point0.x * (1 - t) * (1 - t) * (1 - t) + 3 * mPointF1.x * t * (1 - t) * (1 - t)
                + 3 * mPointF2.x * t * t * (1 - t) + point3.x * t * t * t;
        pointF.y = point0.y * (1 - t) * (1 - t) * (1 - t) + 3 * mPointF1.y * t * (1 - t) * (1 - t)
                + 3 * mPointF2.y * t * t * (1 - t) + point3.y * t * t * t;
        return pointF;
    }
}
