package app.cheftastic.vanilla.widgets;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.ArcShape;
import android.graphics.drawable.shapes.OvalShape;
import android.os.AsyncTask;
import android.util.AttributeSet;
import android.view.View;

import app.cheftastic.R;


public class CircularGraph extends View {

    public final static int DARK = 0;
    public final static int LIGHT = 1;

    private final static int DEGREES_TARGET = 0;
    private final static int DEGREES_SAMPLE = 1;
    private final static int DEGREES_OVERFLOW = 2;
    ShapeDrawable[][] layers;
    ShapeDrawable centralCircle;
    private int alphaTarget = 80;
    private int alphaOverflow = 160;
    private int height = 0;
    private int width = 0;
    private int margin = 2;
    private int graphWidth = 50;
    private float borderWidth = 3f;
    private float separation = 1f;
    private double[] percentagesTarget;
    private double[] percentagesSample;
    private double[] tPercentagesSample;
    private int[][] colors;

    private CircularGraphAnimation animation;


    public CircularGraph(Context context) {
        this(context, null);
    }

    public CircularGraph(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircularGraph(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        colors = new int[2][8];
        colors[LIGHT][0] = context.getResources().getColor(R.color.blue_light);
        colors[DARK][0] = context.getResources().getColor(R.color.blue_dark);
        colors[LIGHT][1] = context.getResources().getColor(R.color.purple_light);
        colors[DARK][1] = context.getResources().getColor(R.color.purple_dark);
        colors[LIGHT][2] = context.getResources().getColor(R.color.green_light);
        colors[DARK][2] = context.getResources().getColor(R.color.green_dark);
        colors[LIGHT][3] = context.getResources().getColor(R.color.orange_light);
        colors[DARK][3] = context.getResources().getColor(R.color.orange_dark);
        colors[LIGHT][4] = context.getResources().getColor(R.color.emerald_light);
        colors[DARK][4] = context.getResources().getColor(R.color.emerald_dark);
        colors[LIGHT][5] = context.getResources().getColor(R.color.red_light);
        colors[DARK][5] = context.getResources().getColor(R.color.red_dark);
        colors[LIGHT][6] = context.getResources().getColor(R.color.yellow_light);
        colors[DARK][6] = context.getResources().getColor(R.color.yellow_dark);
        colors[LIGHT][7] = context.getResources().getColor(R.color.pink_light);
        colors[DARK][7] = context.getResources().getColor(R.color.pink_dark);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int minWidth = getPaddingLeft() + getPaddingRight() + getSuggestedMinimumWidth();
        int width = resolveSizeAndState(minWidth, widthMeasureSpec, 1);

        int height = resolveSizeAndState(MeasureSpec.getSize(width), heightMeasureSpec, 0);

        setMeasuredDimension(Math.min(width, height), Math.min(width, height));
    }

    public boolean setPercentagesSample(double[] sample) {
        return setPercentages(this.percentagesTarget, sample);
    }

    public boolean setPercentages(double[] target, double[] sample) {
        if (target == null && sample == null) {
            return false;
        }

        int numData;
        if (target != null) {
            numData = target.length;
            if (sample != null) {
                if (target.length != sample.length) {
                    return false;
                }
            }
        } else {
            numData = sample.length;
        }

        double sumPercentagesTarget = 0d;
        if (target != null) {
            for (double percentage : target) {
                sumPercentagesTarget += percentage;
            }
            if (sumPercentagesTarget != 100 && sumPercentagesTarget != 1) {
                return false;
            }
        }

        double sumPercentagesSample = 0d;
        if (sample != null) {
            for (double percentage : sample) {
                sumPercentagesSample += percentage;
            }
            if ((Math.abs(sumPercentagesSample - 100) >= 0.01) && (Math.abs(sumPercentagesSample - 1) >= 0.001) && sumPercentagesSample != 0) {
                return false;
            }
        }

        if (target == null && sumPercentagesSample == 0) {
            return false;
        }

        if (this.percentagesSample != null) {
            tPercentagesSample = new double[this.percentagesSample.length];
            System.arraycopy(this.percentagesSample, 0, tPercentagesSample, 0, this.percentagesSample.length);
        }

        this.percentagesTarget = new double[numData];
        this.percentagesSample = new double[numData];

        if (sumPercentagesTarget == 100) {
            for (int i = 0; i < target.length; i++) {
                this.percentagesTarget[i] = target[i] / 100d;
            }
        } else if (target != null) {
            System.arraycopy(target, 0, this.percentagesTarget, 0, target.length);
        }

        if (sumPercentagesSample == 100) {
            for (int i = 0; i < sample.length; i++) {
                this.percentagesTarget[i] = sample[i] / 100d;
            }
        } else if (sample != null) {
            System.arraycopy(sample, 0, this.percentagesSample, 0, sample.length);
        } else {
            for (int i = 0; i < this.percentagesSample.length; i++) {
                this.percentagesSample[i] = 0d;
            }
        }

        if (target == null) {
            System.arraycopy(this.percentagesSample, 0, this.percentagesTarget, 0, this.percentagesSample.length);
        }

        if (animation != null) {
            animation.cancel(true);
        }

        animation = new CircularGraphAnimation();
        animation.execute();
        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (width == 0 && height == 0) {
            width = canvas.getWidth();
            height = canvas.getHeight();

            drawGraph(percentagesTarget, percentagesSample);

            invalidate();
        } else {
            Bitmap bitmap = Bitmap.createBitmap(canvas.getWidth(), canvas.getHeight(), Bitmap.Config.ARGB_8888);
            Canvas t_canvas = new Canvas(bitmap);

            if (layers != null) {
                for (ShapeDrawable[] layer : layers) {
                    for (ShapeDrawable shape : layer) {
                        if (shape != null) {
                            shape.draw(t_canvas);
                        }
                    }
                }
            }

            if (centralCircle != null) {
                centralCircle.draw(t_canvas);
            }

            canvas.drawBitmap(bitmap, 0, 0, null);
        }
    }

    private void drawGraph(double[] target, double[] sample) {
        if (height == 0 || width == 0) {
            return;
        }

        boolean drawSample = true;
        if (target == null || sample == null || sample.length != target.length) {
            target = new double[]{1d};
            sample = new double[]{0d};
            drawSample = false;
        }

        double sumPercentagesSample = 0;
        for (double percentage : sample) {
            sumPercentagesSample += percentage;
        }
        if (sumPercentagesSample == 0) {
            drawSample = false;
        }

        float separation = this.separation;
        this.separation = (target.length == 1) ? 0 : this.separation;

        float[][] degrees = new float[3][target.length];
        layers = new ShapeDrawable[6][target.length];
        int degreesConsumed;
        int size = Math.min(width, height);

        for (int i = 0; i < target.length; i++) {
            degrees[DEGREES_TARGET][i] = (float) target[i] * 360;
        }

        for (int i = 0; i < sample.length; i++) {
            degrees[DEGREES_SAMPLE][i] = (float) Math.min(sample[i], target[i]) * 360;
        }

        for (int i = 0; i < sample.length; i++) {
            if (sample[i] > target[i]) {
                degrees[DEGREES_OVERFLOW][i] = (float) Math.min(Math.max(0, sample[i] - target[i]), target[i]) * 360;
            } else {
                degrees[DEGREES_OVERFLOW][i] = 0;
            }
        }

        degreesConsumed = 0;
        ShapeDrawable[] shapes = layers[0];
        for (int i = 0; i < shapes.length; i++) {
            shapes[i] = new ShapeDrawable();
            shapes[i].setShape(new ArcShape(degreesConsumed + this.separation, degrees[DEGREES_TARGET][i] - (this.separation * 2)));
            shapes[i].getPaint().setColor(colors[LIGHT][i % colors[LIGHT].length]);
            shapes[i].setAlpha(alphaTarget);
            shapes[i].setBounds(margin, margin, size - margin, size - margin);
            degreesConsumed += degrees[DEGREES_TARGET][i];
        }

        // Layer #1: External border
        if (drawSample) {
            degreesConsumed = 0;
            shapes = layers[1];
            for (int i = 0; i < shapes.length; i++) {
                if (degrees[DEGREES_SAMPLE][i] > this.separation * 2) {
                    shapes[i] = new ShapeDrawable();
                    shapes[i].setShape(new ArcShape(degreesConsumed + this.separation, degrees[DEGREES_SAMPLE][i] - (this.separation * 2)));
                    shapes[i].getPaint().setColor(colors[DARK][i % colors[DARK].length]);
                    shapes[i].setBounds(margin, margin, size - margin, size - margin);
                }
                degreesConsumed += degrees[DEGREES_TARGET][i];
            }

            // Layer #2: Sample
            degreesConsumed = 0;
            shapes = layers[3];
            for (int i = 0; i < shapes.length; i++) {
                if (degrees[DEGREES_SAMPLE][i] > borderWidth) {
                    shapes[i] = new ShapeDrawable();
                    shapes[i].setShape(new ArcShape(degreesConsumed + borderWidth / 2, degrees[DEGREES_SAMPLE][i] - borderWidth));
                    shapes[i].getPaint().setColor(colors[LIGHT][i % colors[LIGHT].length]);
                    shapes[i].setBounds(margin + Math.round(borderWidth), margin + Math.round(borderWidth), size - margin - Math.round(borderWidth), size - margin - Math.round(borderWidth));
                }
                degreesConsumed += degrees[DEGREES_TARGET][i];
            }

            // Layer #3: Overflow
            degreesConsumed = 0;
            shapes = layers[4];
            for (int i = 0; i < shapes.length; i++) {
                if (degrees[DEGREES_SAMPLE][i] > this.separation * 2) {
                    shapes[i] = new ShapeDrawable();
                    shapes[i].setShape(new ArcShape(degreesConsumed + this.separation, Math.min(Math.max(degrees[DEGREES_OVERFLOW][i] - 1, 0), degrees[DEGREES_TARGET][i] - (this.separation * 2))));
                    shapes[i].getPaint().setColor(colors[DARK][i % colors[DARK].length]);
                    shapes[i].setAlpha(alphaOverflow);
                    shapes[i].setBounds(margin, margin, size - margin, size - margin);
                }
                degreesConsumed += degrees[DEGREES_TARGET][i];
            }

            // Layer #4: Internal border
            degreesConsumed = 0;
            shapes = layers[5];
            for (int i = 0; i < shapes.length; i++) {
                if (degrees[DEGREES_SAMPLE][i] > 0d) {
                    shapes[i] = new ShapeDrawable();
                    shapes[i].setShape(new ArcShape(degreesConsumed + this.separation, Math.max(degrees[DEGREES_SAMPLE][i] - (this.separation * 2), 0)));
                    shapes[i].getPaint().setColor(colors[DARK][i % colors[DARK].length]);
                    shapes[i].setBounds(margin + graphWidth - Math.round(borderWidth), margin + graphWidth - Math.round(borderWidth), size - (margin + graphWidth - Math.round(borderWidth)), size - (margin + graphWidth - Math.round(borderWidth)));
                }
                degreesConsumed += degrees[DEGREES_TARGET][i];
            }
        }

        // Central circle
        centralCircle = new ShapeDrawable();
        centralCircle.setShape(new OvalShape());
        centralCircle.getPaint().setColor(0xFFFFFFFF);
        centralCircle.getPaint().setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        centralCircle.getPaint().setAntiAlias(true);
        centralCircle.setBounds(margin + graphWidth, margin + graphWidth, size - margin - graphWidth, size - margin - graphWidth);

        this.separation = separation;
    }

    private class CircularGraphAnimation extends AsyncTask<Void, Void, Void> {
        private static final int duration = 1500;
        private static final int steps = 100;

        @Override
        protected Void doInBackground(Void... params) {
            if (percentagesSample != null && tPercentagesSample != null && percentagesSample.length == tPercentagesSample.length) {
                double[] diffs = new double[tPercentagesSample.length];
                for (int i = 0; i < diffs.length; i++) {
                    diffs[i] = percentagesSample[i] - tPercentagesSample[i];
                }
                publishProgress();

                for (int i = 0; i < steps; i++) {
                    try {
                        Thread.sleep(duration / steps);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    for (int j = 0; j < percentagesSample.length; j++) {
                        tPercentagesSample[j] += diffs[j] / steps;
                    }

                    publishProgress();
                }
            }

            return null;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
            drawGraph(percentagesTarget, tPercentagesSample);
            invalidate();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            drawGraph(percentagesTarget, percentagesSample);
            invalidate();
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            drawGraph(percentagesTarget, percentagesSample);
            invalidate();
        }
    }
}
