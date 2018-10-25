package uk.adamwoollen.antennaarray;

import java.awt.*;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import javax.swing.*;

class Surface extends JPanel
{
    double[][] swarmPositions;

    private void doDrawing(Graphics g)
    {
        Graphics2D g2d = (Graphics2D) g;

        double scale = 500;
        int padding = 50;
        int particleWidth = 10;
        double problemSpace = 1.5;

        g2d.drawString("Antenna 1", (int) (padding + (scale * problemSpace / 2)), 15);

        int incrementDivider = 15;
        {
            double value = 0;
            double x = padding - 8;
            DecimalFormat df = new DecimalFormat("0.0");
            df.setRoundingMode(RoundingMode.HALF_UP);

            for (int i = 0; i < incrementDivider + 1; i++)
            {
                g2d.drawString(df.format(value), (int) x, padding - 5);
                value += problemSpace / incrementDivider;
                x += scale * 1.5 / incrementDivider;
            }
        }
        g2d.rotate(Math.PI/2);
        {
            g2d.drawString("Antenna 2", (int) (padding + (scale * problemSpace / 2)), -5);
            
            double value = 0;
            double x = padding - 8;
            DecimalFormat df = new DecimalFormat("0.0");
            df.setRoundingMode(RoundingMode.HALF_UP);

            for (int i = 0; i < incrementDivider + 1; i++)
            {
                g2d.drawString(df.format(value), (int) x, 12 - padding);
                value += problemSpace / incrementDivider;
                x += scale * 1.5 / incrementDivider;
            }
        }
        g2d.rotate(-Math.PI/2);

        g2d.drawLine(padding, padding, padding + (int)(problemSpace * scale), padding);// L -> R
        g2d.drawLine(padding, padding, padding, padding + (int)(problemSpace * scale));// T -> B

        if (swarmPositions != null)
        {
            for (int i = 0; i < swarmPositions.length; i++)
            {
                double[] particlePosition = swarmPositions[i];
                Color colour = i < colours.length ? colours[i] : Color.black;

                g2d.setColor(colour);
                double[] position = particlePosition;
                g2d.fillOval(padding + (int)(particlePosition[0] * scale), padding + (int)(particlePosition[1] * scale), particleWidth, particleWidth);
            }
        }
    }

    @Override
    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        doDrawing(g);
    }

    public void updatePositions(double[][] swarmPositions)
    {
        this.swarmPositions = swarmPositions;
        repaint();
    }

    private Color[] colours = new Color[]{
            new Color(255, 0, 0),
            new Color(0, 255, 0),
            new Color(0, 0, 255),
            new Color(255, 125, 0),
            new Color(255, 0, 255),
            new Color(0, 255, 255),


            new Color(230, 25, 75),
            new Color(60, 180, 75),
            new Color(255, 225, 25),
            new Color(0, 130, 200),
            new Color(245, 130, 48),
            new Color(145, 30, 180),
            new Color(70, 240, 240),
            new Color(240, 50, 230),
            new Color(210, 245, 60),
            new Color(250, 190, 190),
            new Color(0, 128, 128),
            new Color(230, 190, 255),
            new Color(170, 110, 40),
            new Color(255, 250, 200),
            new Color(128, 0, 0),
            new Color(170, 255, 195),
            new Color(128, 128, 0),
            new Color(255, 215, 180)
    };
}

class BasicEx extends JFrame
{
    private Surface surface;

    public BasicEx()
    {
        initUI();
    }

    private void initUI()
    {
        surface = new Surface();
        add(surface);

        setTitle("Particle Swarm Optimisation");
        setSize(1000, 1000);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public void updatePositions(double[][] swarmPositions)
    {
        surface.updatePositions(swarmPositions);
    }
}



