package Turtle;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;
import java.awt.geom.Point2D.Double;
import java.awt.image.BufferedImage;
import java.awt.image.DirectColorModel;
import java.awt.image.ImageObserver;
import java.awt.image.WritableRaster;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.Map.Entry;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JApplet;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;
import javax.swing.filechooser.FileNameExtensionFilter;

public class Turtle implements Runnable, ActionListener, MouseListener, MouseMotionListener, KeyListener, ComponentListener, MouseWheelListener {
    private static ArrayList<Turtle> turtles;
    private static TreeMap<Long, ArrayList> turtleStates;
    private static TreeMap<Long, ArrayList> redoStates;
    private static JFrame window;
    private static JApplet applet;
    private static JLabel draw;
    private static int width;
    private static int height;
    private static BufferedImage offscreenImage;
    private static BufferedImage midscreenImage;
    private static BufferedImage onscreenImage;
    private static Graphics2D offscreen;
    private static Graphics2D midscreen;
    private static Graphics2D onscreen;
    private static BufferedImage backgroundImage;
    private static Color backgroundColor;
    private static ImageIcon icon;
    private static Turtle turtle;
    private static HashMap<String, Polygon> shapes;
    private static HashMap<String, Color> colors;
    private static HashMap<String, ArrayList<ArrayList>> keyBindings;
    private static HashMap<Turtle, ArrayList<ArrayList>> mouseBindings;
    private static double centerX;
    private static double centerY;
    private static double scale;
    private static TreeSet<String> keysDown;
    private static TreeSet<String> processedKeys;
    private static TreeSet<String> unprocessedKeys;
    private static long lastUpdate;
    private static long fps;
    private static final Object turtleLock = new Object();
    private static int dragx = 0;
    private static int dragy = 0;
    private static int x = 0;
    private static int y = 0;
    private static int modifiers = 0;
    private static final Object keyLock = new Object();
    private static final int REFRESH_MODE_ANIMATED = 0;
    private static final int REFRESH_MODE_STATE_CHANGE = 1;
    private static final int REFRESH_MODE_ON_DEMAND = 2;
    private static int refreshMode;
    private static final int BACKGROUND_MODE_STRETCH = 0;
    private static final int BACKGROUND_MODE_CENTER = 1;
    private static final int BACKGROUND_MODE_TILE = 2;
    private static final int BACKGROUND_MODE_CENTER_RELATIVE = 3;
    private static final int BACKGROUND_MODE_TILE_RELATIVE = 4;
    private static int backgroundMode;
    private static Turtle selectedTurtle;
    private static boolean running;
    private Double location = new Double(0.0D, 0.0D);
    private double direction = 0.0D;
    private String shape = "turtle";
    private BufferedImage image = null;
    private double shapeWidth = 33.0D;
    private double shapeHeight = 33.0D;
    private double tilt = 0.0D;
    private double penWidth = 2.0D;
    private Color penColor;
    private double outlineWidth;
    private Color outlineColor;
    private Color fillColor;
    private double speed;
    private boolean isPenDown;
    private boolean isFilling;
    private boolean isVisible;
    private ArrayList<Double> polygon;
    private Long _time;
    private Double _location;
    private java.lang.Double _direction;
    private String _shape;
    private BufferedImage _image;
    private java.lang.Double _shapeWidth;
    private java.lang.Double _shapeHeight;
    private java.lang.Double _tilt;
    private java.lang.Double _penWidth;
    private Color _penColor;
    private java.lang.Double _outlineWidth;
    private Color _outlineColor;
    private Color _fillColor;
    private java.lang.Double _speed;
    private Boolean _isPenDown;
    private Boolean _isFilling;
    private Boolean _isVisible;
    private ArrayList<Double> _polygon;
    private Boolean _isFill;
    private Boolean _isStamp;
    private java.lang.Double _dotSize;
    private Color _dotColor;
    private Font _font;
    private String _text;
    private Integer _justification;
    private Double _textOffset;
    private Long __time;
    private Double __location;
    private java.lang.Double __direction;
    private String __shape;
    private BufferedImage __image;
    private java.lang.Double __shapeWidth;
    private java.lang.Double __shapeHeight;
    private java.lang.Double __tilt;
    private java.lang.Double __penWidth;
    private Color __penColor;
    private java.lang.Double __outlineWidth;
    private Color __outlineColor;
    private Color __fillColor;
    private java.lang.Double __speed;
    private Boolean __isPenDown;
    private Boolean __isFilling;
    private Boolean __isVisible;
    private ArrayList<Double> __polygon;
    private Boolean __isFill;
    private Boolean __isStamp;
    private java.lang.Double __dotSize;
    private Color __dotColor;
    private Font __font;
    private String __text;
    private Integer __justification;
    private Double __textOffset;

    public void run() {
        if (Thread.currentThread().getName().equals("render")) {
            this.renderLoop();
        } else if (Thread.currentThread().getName().equals("listen")) {
            this.eventLoop();
        }

    }

    private void eventLoop() {
        long var1 = 0L;

        while(running) {
            var1 = System.nanoTime();
            this.processKeys();
            waitUntil(var1 + 1000000000L / fps);
        }

    }

    private void renderLoop() {
        long var1 = 0L;

        while(running) {
            var1 = System.nanoTime();
            if (refreshMode == 0) {
                draw();
            }

            if (!waitUntil(var1 + 1000000000L / fps)) {
                --fps;
            } else if (fps < 30L) {
                ++fps;
            }
        }

    }

    private static boolean waitUntil(Long var0) {
        long var1 = System.nanoTime();
        if (var1 < var0) {
            try {
                Thread.sleep((var0 - var1) / 1000000L);
            } catch (Exception var4) {
            }

            return true;
        } else {
            return false;
        }
    }

    private static void init() {
        turtles = new ArrayList();
        turtleStates = new TreeMap();
        redoStates = new TreeMap();
        width = 500;
        height = 500;
        backgroundColor = Color.WHITE;
        keyBindings = new HashMap();
        mouseBindings = new HashMap();
        centerX = 0.0D;
        centerY = 0.0D;
        scale = 1.0D;
        keysDown = new TreeSet();
        processedKeys = new TreeSet();
        unprocessedKeys = new TreeSet();
        lastUpdate = 0L;
        fps = 30L;
        dragx = 0;
        dragy = 0;
        x = 0;
        y = 0;
        modifiers = 0;
        refreshMode = 0;
        backgroundMode = 4;
        selectedTurtle = null;
        running = true;
        window = new JFrame("Turtle");
        icon = new ImageIcon();
        setupBuffering();
        draw = new JLabel(icon);
        window.setContentPane(draw);

        try {
            window.setDefaultCloseOperation(3);
        } catch (Exception var3) {
        }

        JMenuBar var0 = new JMenuBar();
        JMenu var1 = new JMenu("File");
        var0.add(var1);
        JMenuItem var2 = new JMenuItem("Save...");
        var2.setAccelerator(KeyStroke.getKeyStroke(83, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
        var1.add(var2);
        window.setJMenuBar(var0);
        window.pack();
        window.requestFocusInWindow();
        drawTurtleIcon();
        window.setVisible(true);
        makeShapes();
        turtle = new Turtle(0);
        draw.setFocusable(true);
        var2.addActionListener(turtle);
        window.addComponentListener(turtle);
        draw.addComponentListener(turtle);
        draw.addMouseListener(turtle);
        draw.addMouseMotionListener(turtle);
        draw.addMouseWheelListener(turtle);
        window.addKeyListener(turtle);
        draw.addKeyListener(turtle);
        draw.requestFocus();
        initColors();
        (new Thread(turtle, "render")).start();
        (new Thread(turtle, "listen")).start();
    }

    public static void exit() {
        running = false;
        window.setVisible(false);
        window.dispose();
    }

    public static void startApplet(JApplet var0) {
        applet = var0;
        var0.setContentPane(window.getContentPane());
        window.setVisible(false);

        try {
            (new Thread((Runnable)var0, "turtle")).start();
        } catch (Exception var2) {
            var2.printStackTrace();
        }

    }

    private static void initColors() {
        colors = new HashMap();
        colors.put("aliceblue", new Color(15792383));
        colors.put("antiquewhite", new Color(16444375));
        colors.put("aqua", new Color(65535));
        colors.put("aquamarine", new Color(8388564));
        colors.put("azure", new Color(15794175));
        colors.put("beige", new Color(16119260));
        colors.put("bisque", new Color(16770244));
        colors.put("black", new Color(0));
        colors.put("blanchedalmond", new Color(16772045));
        colors.put("blue", new Color(255));
        colors.put("blueviolet", new Color(9055202));
        colors.put("brown", new Color(10824234));
        colors.put("burlywood", new Color(14596231));
        colors.put("cadetblue", new Color(6266528));
        colors.put("chartreuse", new Color(8388352));
        colors.put("chocolate", new Color(13789470));
        colors.put("coral", new Color(16744272));
        colors.put("cornflowerblue", new Color(6591981));
        colors.put("cornsilk", new Color(16775388));
        colors.put("crimson", new Color(14423100));
        colors.put("cyan", new Color(65535));
        colors.put("darkblue", new Color(139));
        colors.put("darkcyan", new Color(35723));
        colors.put("darkgoldenrod", new Color(12092939));
        colors.put("darkgray", new Color(11119017));
        colors.put("darkgreen", new Color(25600));
        colors.put("darkkhaki", new Color(12433259));
        colors.put("darkmagenta", new Color(9109643));
        colors.put("darkolivegreen", new Color(5597999));
        colors.put("darkorange", new Color(16747520));
        colors.put("darkorchid", new Color(10040012));
        colors.put("darkred", new Color(9109504));
        colors.put("darksalmon", new Color(15308410));
        colors.put("darkseagreen", new Color(9419919));
        colors.put("darkslateblue", new Color(4734347));
        colors.put("darkslategray", new Color(3100495));
        colors.put("darkturquoise", new Color(52945));
        colors.put("darkviolet", new Color(9699539));
        colors.put("deeppink", new Color(16716947));
        colors.put("deepskyblue", new Color(49151));
        colors.put("dimgray", new Color(6908265));
        colors.put("dodgerblue", new Color(2003199));
        colors.put("firebrick", new Color(11674146));
        colors.put("floralwhite", new Color(16775920));
        colors.put("forestgreen", new Color(2263842));
        colors.put("fuchsia", new Color(16711935));
        colors.put("gainsboro", new Color(14474460));
        colors.put("ghostwhite", new Color(16316671));
        colors.put("gold", new Color(16766720));
        colors.put("goldenrod", new Color(14329120));
        colors.put("gray", new Color(8421504));
        colors.put("green", new Color(32768));
        colors.put("greenyellow", new Color(11403055));
        colors.put("honeydew", new Color(15794160));
        colors.put("hotpink", new Color(16738740));
        colors.put("indianred", new Color(13458524));
        colors.put("indigo", new Color(4915330));
        colors.put("ivory", new Color(16777200));
        colors.put("khaki", new Color(15787660));
        colors.put("lavender", new Color(15132410));
        colors.put("lavenderblush", new Color(16773365));
        colors.put("lawngreen", new Color(8190976));
        colors.put("lemonchiffon", new Color(16775885));
        colors.put("lightblue", new Color(11393254));
        colors.put("lightcoral", new Color(15761536));
        colors.put("lightcyan", new Color(14745599));
        colors.put("lightgoldenrodyellow", new Color(16448210));
        colors.put("lightgreen", new Color(9498256));
        colors.put("lightgrey", new Color(13882323));
        colors.put("lightpink", new Color(16758465));
        colors.put("lightsalmon", new Color(16752762));
        colors.put("lightseagreen", new Color(2142890));
        colors.put("lightskyblue", new Color(8900346));
        colors.put("lightslategray", new Color(7833753));
        colors.put("lightsteelblue", new Color(11584734));
        colors.put("lightyellow", new Color(16777184));
        colors.put("lime", new Color(65280));
        colors.put("limegreen", new Color(3329330));
        colors.put("linen", new Color(16445670));
        colors.put("magenta", new Color(16711935));
        colors.put("maroon", new Color(8388608));
        colors.put("mediumaquamarine", new Color(6737322));
        colors.put("mediumblue", new Color(205));
        colors.put("mediumorchid", new Color(12211667));
        colors.put("mediumpurple", new Color(9662683));
        colors.put("mediumseagreen", new Color(3978097));
        colors.put("mediumslateblue", new Color(8087790));
        colors.put("mediumspringgreen", new Color(64154));
        colors.put("mediumturquoise", new Color(4772300));
        colors.put("mediumvioletred", new Color(13047173));
        colors.put("midnightblue", new Color(1644912));
        colors.put("mintcream", new Color(16121850));
        colors.put("mistyrose", new Color(16770273));
        colors.put("moccasin", new Color(16770229));
        colors.put("navajowhite", new Color(16768685));
        colors.put("navy", new Color(128));
        colors.put("oldlace", new Color(16643558));
        colors.put("olive", new Color(8421376));
        colors.put("olivedrab", new Color(7048739));
        colors.put("orange", new Color(16753920));
        colors.put("orangered", new Color(16729344));
        colors.put("orchid", new Color(14315734));
        colors.put("palegoldenrod", new Color(15657130));
        colors.put("palegreen", new Color(10025880));
        colors.put("paleturquoise", new Color(11529966));
        colors.put("palevioletred", new Color(14381203));
        colors.put("papayawhip", new Color(16773077));
        colors.put("peachpuff", new Color(16767673));
        colors.put("peru", new Color(13468991));
        colors.put("pink", new Color(16761035));
        colors.put("plum", new Color(14524637));
        colors.put("powderblue", new Color(11591910));
        colors.put("purple", new Color(8388736));
        colors.put("red", new Color(16711680));
        colors.put("rosybrown", new Color(12357519));
        colors.put("royalblue", new Color(4286945));
        colors.put("saddlebrown", new Color(9127187));
        colors.put("salmon", new Color(16416882));
        colors.put("sandybrown", new Color(16032864));
        colors.put("seagreen", new Color(3050327));
        colors.put("seashell", new Color(16774638));
        colors.put("sienna", new Color(10506797));
        colors.put("silver", new Color(12632256));
        colors.put("skyblue", new Color(8900331));
        colors.put("slateblue", new Color(6970061));
        colors.put("slategray", new Color(7372944));
        colors.put("snow", new Color(16775930));
        colors.put("springgreen", new Color(65407));
        colors.put("steelblue", new Color(4620980));
        colors.put("tan", new Color(13808780));
        colors.put("teal", new Color(32896));
        colors.put("thistle", new Color(14204888));
        colors.put("tomato", new Color(16737095));
        colors.put("turquoise", new Color(4251856));
        colors.put("violet", new Color(15631086));
        colors.put("wheat", new Color(16113331));
        colors.put("white", new Color(16777215));
        colors.put("whitesmoke", new Color(16119285));
        colors.put("yellow", new Color(16776960));
        colors.put("yellowgreen", new Color(10145074));
    }

    private static Color getColor(String var0) {
        var0 = var0.toLowerCase().replaceAll("[^a-z]", "");
        return colors.containsKey(var0) ? (Color)colors.get(var0) : null;
    }

    private static void setupBuffering() {
        synchronized(turtleLock) {
            lastUpdate = 0L;
            offscreenImage = new BufferedImage(width, height, 2);
            midscreenImage = new BufferedImage(width, height, 2);
            onscreenImage = new BufferedImage(width, height, 2);
            offscreen = offscreenImage.createGraphics();
            midscreen = midscreenImage.createGraphics();
            onscreen = onscreenImage.createGraphics();
            offscreen.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            midscreen.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            onscreen.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            drawBackground(offscreen);
            drawBackground(onscreen);
            icon.setImage(onscreenImage);
        }
    }

    private static void drawTurtleIcon() {
        byte[] var0 = new byte[]{71, 73, 70, 56, 57, 97, 16, 0, 16, 0, -95, 2, 0, 0, -103, 0, 0, -1, 0, -1, -1, -1, -1, -1, -1, 33, -7, 4, 1, 10, 0, 2, 0, 44, 0, 0, 0, 0, 16, 0, 16, 0, 0, 2, 44, -108, -113, -87, -53, -19, -33, -128, 4, 104, 74, 35, 67, -72, 34, -21, 11, 124, 27, -90, -107, -109, 72, 117, -91, -71, 110, 103, -37, 90, -31, -10, -55, -87, 122, -34, 74, 72, -15, 17, -56, -127, 8, 33, 5, 0, 59};

        try {
            BufferedImage var1 = ImageIO.read(new ByteArrayInputStream(var0));
            window.setIconImage(var1);
        } catch (Exception var2) {
        }

    }

    private static void makeShapes() {
        shapes = new HashMap();
        int[] var0 = new int[]{66, 65, 63, 61, 53, 44, 33, 24, 23, 19, 17, 14, 9, 8, 8, 10, 13, 11, 10, 2, 9, 11, 15, 11, 11, 10, 12, 18, 20, 22, 23, 26, 35, 44, 53, 61, 62, 64, 66, 71, 77, 78, 77, 76, 72, 77, 81, 86, 91, 94, 97, 98, 97, 95, 92, 87, 82, 77, 72, 74, 77, 78, 76, 70};
        int[] var1 = new int[]{18, 19, 21, 25, 23, 22, 23, 27, 25, 21, 20, 21, 27, 30, 32, 34, 37, 42, 47, 50, 53, 59, 65, 68, 69, 71, 74, 79, 80, 80, 78, 74, 77, 78, 77, 75, 79, 81, 82, 81, 76, 73, 71, 69, 66, 59, 60, 61, 60, 58, 54, 50, 46, 42, 40, 39, 40, 41, 34, 32, 28, 27, 24, 19};
        Polygon var2 = new Polygon(var0, var1, var0.length);
        shapes.put("turtle", var2);
        var0 = new int[]{0, 100, 0, 20};
        var1 = new int[]{0, 50, 100, 50};
        var2 = new Polygon(var0, var1, var0.length);
        shapes.put("arrow", var2);
        var0 = new int[]{0, 100, 100, 0};
        var1 = new int[]{0, 0, 100, 100};
        var2 = new Polygon(var0, var1, var0.length);
        shapes.put("rectangle", var2);
        shapes.put("square", var2);
        var0 = new int[]{0, 100, 0};
        var1 = new int[]{0, 50, 100};
        var2 = new Polygon(var0, var1, var0.length);
        shapes.put("triangle", var2);
        byte var3 = 24;
        var0 = new int[var3];
        var1 = new int[var3];

        for(int var4 = 0; var4 < var3; ++var4) {
            double var5 = Math.toRadians((double)var4 * 360.0D / (double)var3);
            var0[var4] = (int)Math.round(50.0D + 50.0D * Math.cos(var5));
            var1[var4] = (int)Math.round(50.0D + 50.0D * Math.sin(var5));
        }

        var2 = new Polygon(var0, var1, var0.length);
        shapes.put("circle", var2);
    }

    private Turtle(int var1) {
        this.penColor = Color.BLACK;
        this.outlineWidth = 2.0D;
        this.outlineColor = Color.BLACK;
        this.fillColor = new Color(0, 255, 0, 128);
        this.speed = 50.0D;
        this.isPenDown = true;
        this.isFilling = false;
        this.isVisible = true;
        this.polygon = new ArrayList();
    }

    public Turtle() {
        this.penColor = Color.BLACK;
        this.outlineWidth = 2.0D;
        this.outlineColor = Color.BLACK;
        this.fillColor = new Color(0, 255, 0, 128);
        this.speed = 50.0D;
        this.isPenDown = true;
        this.isFilling = false;
        this.isVisible = true;
        this.polygon = new ArrayList();
        if (window == null) {
            init();
        }

        synchronized(turtleLock) {
            turtles.add(this);
        }

        long var1 = this.storeCurrentState();
        updateAll();
    }

    public Turtle(double var1, double var3) {
        this.penColor = Color.BLACK;
        this.outlineWidth = 2.0D;
        this.outlineColor = Color.BLACK;
        this.fillColor = new Color(0, 255, 0, 128);
        this.speed = 50.0D;
        this.isPenDown = true;
        this.isFilling = false;
        this.isVisible = true;
        this.polygon = new ArrayList();
        if (window == null) {
            init();
        }

        this.location = new Double(var1, var3);
        synchronized(turtleLock) {
            turtles.add(this);
        }

        long var5 = this.storeCurrentState();
        updateAll();
    }

    public Turtle clone() {
        Turtle var1 = new Turtle(0);
        var1.location = (Double)this.location.clone();
        var1.direction = this.direction;
        var1.shape = var1.shape;
        var1.image = this.image;
        var1.shapeWidth = this.shapeWidth;
        var1.shapeHeight = this.shapeHeight;
        var1.tilt = this.tilt;
        var1.penWidth = this.penWidth;
        var1.penColor = this.penColor;
        var1.outlineWidth = this.outlineWidth;
        var1.outlineColor = this.outlineColor;
        var1.fillColor = this.fillColor;
        var1.speed = this.speed;
        var1.isPenDown = this.isPenDown;
        var1.isFilling = this.isFilling;
        var1.isVisible = this.isVisible;
        if (window == null) {
            init();
        }

        synchronized(turtleLock) {
            turtles.add(var1);
        }

        long var2 = var1.storeCurrentState();
        return var1;
    }

    private long storeCurrentState() {
        return this.storeCurrentState(false, false, 0.0D, (Color)null, (Font)null, (String)null, 0, (Double)null);
    }

    private long storeAnimatedState() {
        return this.storeCurrentState(true, false, 0.0D, (Color)null, (Font)null, (String)null, 0, (Double)null);
    }

    private long storeCurrentState(boolean var1, boolean var2, double var3, Color var5, Font var6, String var7, int var8, Double var9) {
        ArrayList var10 = new ArrayList();
        long var11 = System.nanoTime();
        synchronized(turtleLock) {
            var10.add(0);
            var10.add(this);
            var10.add(this.location.clone());
            var10.add(this.direction);
            var10.add(this.shape);
            var10.add(this.image);
            var10.add(this.shapeWidth);
            var10.add(this.shapeHeight);
            var10.add(this.tilt);
            var10.add(this.penWidth);
            var10.add(this.penColor);
            var10.add(this.outlineWidth);
            var10.add(this.outlineColor);
            var10.add(this.fillColor);
            var10.add(this.speed);
            var10.add(this.isPenDown);
            var10.add(this.isFilling);
            var10.add(this.isVisible);
            var10.add(var2);
            var10.add(var3);
            var10.add(var5);
            var10.add(var6);
            var10.add(var7);
            var10.add(var8);
            var10.add(var9);
            if (!turtleStates.isEmpty() && (Long)turtleStates.lastKey() > var11) {
                var11 = (Long)turtleStates.lastKey() + 1L;
            }

            if (var1) {
                var11 += (long)(this.speed * 1000000.0D);
            }

            var10.set(0, var11);
            turtleStates.put(var11, var10);
            redoStates.clear();
        }

        if (refreshMode == 1) {
            draw();
        }

        if (refreshMode == 0) {
            waitUntil(var11);
        }

        return var11;
    }

    private static void clearStorage() {
        synchronized(turtleLock) {
            Turtle var2;
            for(Iterator var1 = turtles.iterator(); var1.hasNext(); var2._textOffset = null) {
                var2 = (Turtle)var1.next();
                var2.__time = null;
                var2.__location = null;
                var2.__direction = null;
                var2.__shape = null;
                var2.__image = null;
                var2.__shapeWidth = null;
                var2.__shapeHeight = null;
                var2.__tilt = null;
                var2.__penWidth = null;
                var2.__penColor = null;
                var2.__outlineWidth = null;
                var2.__outlineColor = null;
                var2.__fillColor = null;
                var2.__speed = null;
                var2.__isPenDown = null;
                var2.__isFilling = null;
                var2.__isVisible = null;
                var2.__isStamp = null;
                var2.__dotSize = null;
                var2.__dotColor = null;
                var2.__font = null;
                var2.__text = null;
                var2.__justification = null;
                var2.__textOffset = null;
                var2._time = null;
                var2._location = null;
                var2._direction = null;
                var2._shape = null;
                var2._shapeWidth = null;
                var2._shapeHeight = null;
                var2._image = null;
                var2._tilt = null;
                var2._penWidth = null;
                var2._penColor = null;
                var2._outlineWidth = null;
                var2._outlineColor = null;
                var2._fillColor = null;
                var2._speed = null;
                var2._isPenDown = null;
                var2._isFilling = null;
                var2._isVisible = null;
                var2._isStamp = null;
                var2._dotSize = null;
                var2._dotColor = null;
                var2._font = null;
                var2._text = null;
                var2._justification = null;
            }

        }
    }

    private static void retrieveState(long var0) {
        synchronized(turtleLock) {
            Turtle var3 = getStateTurtle((ArrayList)turtleStates.get(var0));
            var3.__time = var3._time;
            var3.__location = var3._location;
            var3.__direction = var3._direction;
            var3.__shape = var3._shape;
            var3.__image = var3._image;
            var3.__shapeWidth = var3._shapeWidth;
            var3.__shapeHeight = var3._shapeHeight;
            var3.__tilt = var3._tilt;
            var3.__penWidth = var3._penWidth;
            var3.__penColor = var3._penColor;
            var3.__outlineWidth = var3._outlineWidth;
            var3.__outlineColor = var3._outlineColor;
            var3.__fillColor = var3._fillColor;
            var3.__speed = var3._speed;
            var3.__isPenDown = var3._isPenDown;
            var3.__isFilling = var3._isFilling;
            var3.__isVisible = var3._isVisible;
            var3.__isStamp = var3._isStamp;
            var3.__dotSize = var3._dotSize;
            var3.__dotColor = var3._dotColor;
            var3.__font = var3._font;
            var3.__text = var3._text;
            var3.__justification = var3._justification;
            var3.__textOffset = var3._textOffset;
            ArrayList var4 = (ArrayList)turtleStates.get(var0);
            var3._time = getStateTime(var4);
            var3._location = getStateLocation(var4);
            var3._direction = getStateDirection(var4);
            var3._shape = getStateShape(var4);
            var3._shapeWidth = getStateShapeWidth(var4);
            var3._shapeHeight = getStateShapeHeight(var4);
            var3._image = getStateImage(var4);
            var3._tilt = getStateTilt(var4);
            var3._penWidth = getStatePenWidth(var4);
            var3._penColor = getStatePenColor(var4);
            var3._outlineWidth = getStateOutlineWidth(var4);
            var3._outlineColor = getStateOutlineColor(var4);
            var3._fillColor = getStateFillColor(var4);
            var3._speed = getStateSpeed(var4);
            var3._isPenDown = getStateIsPenDown(var4);
            var3._isFilling = getStateIsFilling(var4);
            var3._isVisible = getStateIsVisible(var4);
            var3._isStamp = getStateIsStamp(var4);
            var3._dotSize = getStateDotSize(var4);
            var3._dotColor = getStateDotColor(var4);
            var3._font = getStateFont(var4);
            var3._text = getStateText(var4);
            var3._justification = getStateJustification(var4);
            var3._textOffset = getStateTextOffset(var4);
        }
    }

    private static long getStateTime(ArrayList var0) {
        return (Long)var0.get(0);
    }

    private static Turtle getStateTurtle(ArrayList var0) {
        return (Turtle)var0.get(1);
    }

    private static Double getStateLocation(ArrayList var0) {
        return (Double)((Double)var0.get(2)).clone();
    }

    private static double getStateDirection(ArrayList var0) {
        return (java.lang.Double)var0.get(3);
    }

    private static String getStateShape(ArrayList var0) {
        return (String)var0.get(4);
    }

    private static BufferedImage getStateImage(ArrayList var0) {
        return (BufferedImage)var0.get(5);
    }

    private static double getStateShapeWidth(ArrayList var0) {
        return (java.lang.Double)var0.get(6);
    }

    private static double getStateShapeHeight(ArrayList var0) {
        return (java.lang.Double)var0.get(7);
    }

    private static double getStateTilt(ArrayList var0) {
        return (java.lang.Double)var0.get(8);
    }

    private static double getStatePenWidth(ArrayList var0) {
        return (java.lang.Double)var0.get(9);
    }

    private static Color getStatePenColor(ArrayList var0) {
        return (Color)var0.get(10);
    }

    private static double getStateOutlineWidth(ArrayList var0) {
        return (java.lang.Double)var0.get(11);
    }

    private static Color getStateOutlineColor(ArrayList var0) {
        return (Color)var0.get(12);
    }

    private static Color getStateFillColor(ArrayList var0) {
        return (Color)var0.get(13);
    }

    private static double getStateSpeed(ArrayList var0) {
        return (java.lang.Double)var0.get(14);
    }

    private static boolean getStateIsPenDown(ArrayList var0) {
        return (Boolean)var0.get(15);
    }

    private static boolean getStateIsFilling(ArrayList var0) {
        return (Boolean)var0.get(16);
    }

    private static boolean getStateIsVisible(ArrayList var0) {
        return (Boolean)var0.get(17);
    }

    private static boolean getStateIsStamp(ArrayList var0) {
        return (Boolean)var0.get(18);
    }

    private static double getStateDotSize(ArrayList var0) {
        return (java.lang.Double)var0.get(19);
    }

    private static Color getStateDotColor(ArrayList var0) {
        return (Color)var0.get(20);
    }

    private static Font getStateFont(ArrayList var0) {
        return (Font)var0.get(21);
    }

    private static String getStateText(ArrayList var0) {
        return (String)var0.get(22);
    }

    private static int getStateJustification(ArrayList var0) {
        return (Integer)var0.get(23);
    }

    private static Double getStateTextOffset(ArrayList var0) {
        return (Double)var0.get(24);
    }

    private static void restoreState(long var0) {
        ArrayList var2 = (ArrayList)turtleStates.get(var0);
        Turtle var3 = getStateTurtle((ArrayList)turtleStates.get(var0));
        var3.location = getStateLocation(var2);
        var3.direction = getStateDirection(var2);
        var3.shape = getStateShape(var2);
        var3.shapeWidth = getStateShapeWidth(var2);
        var3.shapeHeight = getStateShapeHeight(var2);
        var3.image = getStateImage(var2);
        var3.tilt = getStateTilt(var2);
        var3.penWidth = getStatePenWidth(var2);
        var3.penColor = getStatePenColor(var2);
        var3.outlineWidth = getStateOutlineWidth(var2);
        var3.outlineColor = getStateOutlineColor(var2);
        var3.fillColor = getStateFillColor(var2);
        var3.speed = getStateSpeed(var2);
        var3.isPenDown = getStateIsPenDown(var2);
        var3.isFilling = getStateIsFilling(var2);
        var3.isVisible = getStateIsVisible(var2);
        if (refreshMode == 1) {
            draw();
        }

    }

    private void select() {
        selectedTurtle = this;
    }

    private void unselect() {
        if (selectedTurtle == this) {
            selectedTurtle = null;
        }

    }

    public boolean contains(int var1, int var2) {
        Double var3 = new Double((double)var1, (double)var2);
        if (this._location == null) {
            return false;
        } else {
            AffineTransform var4 = offscreen.getTransform();
            double var5 = this._location.x;
            double var7 = this._location.y;
            double var9 = this._direction;
            var4.translate((var5 - centerX) * scale + (double)(width / 2), (var7 - centerY) * -scale + (double)(height / 2));
            var4.scale(scale, scale);
            if (this.image == null) {
                var4.rotate(-Math.toRadians(var9));
                var4.scale(this.shapeWidth / 100.0D, this.shapeHeight / 100.0D);
                var4.translate(-50.0D, -50.0D);
                Polygon var16 = (Polygon)shapes.get(this.shape);
                GeneralPath var17 = new GeneralPath();
                var17.append(var16.getPathIterator(var4), false);
                return var17.contains((double)var1, (double)var2);
            } else {
                int var11 = this.image.getWidth();
                int var12 = this.image.getHeight();
                var4.rotate(-Math.toRadians(var9));
                var4.scale(this.shapeWidth / 1.0D / (double)var11, this.shapeHeight / 1.0D / (double)var12);
                var4.translate((double)(-var11 / 2), (double)(-var12 / 2));

                try {
                    var4.inverseTransform(var3, var3);
                } catch (Exception var15) {
                    return false;
                }

                var1 = (int)var3.x;
                var2 = (int)var3.y;

                try {
                    return (new Color(this.image.getRGB(var1, var2), true)).getAlpha() > 50;
                } catch (Exception var14) {
                    return false;
                }
            }
        }
    }

    public double getSpeed() {
        return this.speed;
    }

    public long speed(double var1) {
        this.speed = var1;
        long var3 = this.storeCurrentState();
        return var3;
    }

    public long forward(double var1) {
        double var3 = Math.toRadians(this.direction);
        Double var5 = (Double)this.location.clone();
        Double var10000 = this.location;
        var10000.x += var1 * Math.cos(var3);
        var10000 = this.location;
        var10000.y += var1 * Math.sin(var3);
        long var6 = this.storeAnimatedState();
        return var6;
    }

    public long backward(double var1) {
        double var3 = Math.toRadians(this.direction);
        Double var5 = (Double)this.location.clone();
        Double var10000 = this.location;
        var10000.x -= var1 * Math.cos(var3);
        var10000 = this.location;
        var10000.y -= var1 * Math.sin(var3);
        long var6 = this.storeAnimatedState();
        return var6;
    }

    public long left(double var1) {
        this.direction += var1;
        long var3 = this.storeAnimatedState();
        return var3;
    }

    public long right(double var1) {
        this.direction -= var1;
        long var3 = this.storeAnimatedState();
        return var3;
    }

    public double getDirection() {
        double var1;
        for(var1 = this.direction; var1 >= 360.0D; var1 -= 360.0D) {
        }

        while(var1 < 0.0D) {
            var1 += 360.0D;
        }

        return var1;
    }

    public long setDirection(double var1) {
        double var3;
        for(var3 = var1; this.direction - var3 > 180.0D; var3 += 360.0D) {
        }

        while(this.direction - var3 < -180.0D) {
            var3 -= 360.0D;
        }

        this.direction = var3;
        long var5 = this.storeAnimatedState();
        return var5;
    }

    public long home() {
        return this.setPosition(0.0D, 0.0D, 0.0D);
    }

    public long hide() {
        this.isVisible = false;
        long var1 = this.storeCurrentState();
        return var1;
    }

    public long show() {
        this.isVisible = true;
        long var1 = this.storeCurrentState();
        return var1;
    }

    public long face(double var1, double var3) {
        return this.setDirection(this.towards(var1, var3));
    }

    public double towards(double var1, double var3) {
        return Math.toDegrees(Math.atan2(var3 - this.location.y, var1 - this.location.x));
    }

    public double distance(double var1, double var3) {
        return Math.sqrt((var3 - this.location.y) * (var3 - this.location.y) + (var1 - this.location.x) * (var1 - this.location.x));
    }

    public double getX() {
        return this.location.x;
    }

    public double getY() {
        return this.location.y;
    }

    public long setPosition(double var1, double var3, double var5) {
        this.location.x = var1;
        this.location.y = var3;

        double var7;
        for(var7 = var5; this.direction - var7 > 180.0D; var7 += 360.0D) {
        }

        while(this.direction - var7 < -180.0D) {
            var7 -= 360.0D;
        }

        this.direction = var7;
        this.direction = var5;
        long var9 = this.storeAnimatedState();
        return var9;
    }

    public long setPosition(double var1, double var3) {
        return this.setPosition(var1, var3, this.direction);
    }

    public long tilt(double var1) {
        this.tilt += var1;
        long var3 = this.storeAnimatedState();
        return var3;
    }

    public long setTilt(double var1) {
        this.tilt = var1;
        long var3 = this.storeAnimatedState();
        return var3;
    }

    public double getTilt() {
        return this.tilt;
    }

    public long width(double var1) {
        this.penWidth = var1;
        long var3 = this.storeCurrentState();
        return var3;
    }

    public long outlineWidth(double var1) {
        this.outlineWidth = var1;
        long var3 = this.storeCurrentState();
        return var3;
    }

    public long up() {
        this.isPenDown = false;
        long var1 = this.storeCurrentState();
        return var1;
    }

    public long down() {
        this.isPenDown = true;
        long var1 = this.storeCurrentState();
        return var1;
    }

    public long penColor(String var1) {
        Color var2 = getColor(var1);
        if (var2 != null) {
            this.penColor = var2;
        }

        long var3 = this.storeCurrentState();
        return var3;
    }

    public long penColor(Color var1) {
        this.penColor = var1;
        long var2 = this.storeCurrentState();
        return var2;
    }

    public long outlineColor(String var1) {
        Color var2 = getColor(var1);
        if (var2 != null) {
            this.outlineColor = var2;
        }

        long var3 = this.storeCurrentState();
        return var3;
    }

    public long outlineColor(Color var1) {
        this.outlineColor = var1;
        long var2 = this.storeCurrentState();
        return var2;
    }

    public long fillColor(String var1) {
        Color var2 = getColor(var1);
        if (var2 != null) {
            this.fillColor = var2;
        }

        long var3 = this.storeCurrentState();
        return var3;
    }

    public long fillColor(Color var1) {
        this.fillColor = var1;
        long var2 = this.storeCurrentState();
        return var2;
    }

    public long shape(String var1) {
        try {
            this.image = ImageIO.read(new File(var1));
            this.shapeHeight = (double)this.image.getHeight();
            this.shapeWidth = (double)this.image.getWidth();
        } catch (Exception var4) {
            if (shapes.containsKey(var1)) {
                this.shape = var1;
                this.shapeHeight = 33.0D;
                this.shapeWidth = 33.0D;
                this.image = null;
            } else {
                System.out.println("Unrecognized filename or shape name.");
            }
        }

        long var2 = this.storeCurrentState();
        return var2;
    }

    public long shapeSize(int var1, int var2) {
        this.shapeHeight = (double)var2;
        this.shapeWidth = (double)var1;
        long var3 = this.storeCurrentState();
        return var3;
    }

    public long stamp() {
        long var1 = this.storeCurrentState(true, true, 0.0D, (Color)null, (Font)null, (String)null, 0, (Double)null);
        return var1;
    }

    public long dot() {
        long var1 = this.storeCurrentState(true, false, this.penWidth * 3.0D, this.penColor, (Font)null, (String)null, 0, (Double)null);
        return var1;
    }

    public long dot(String var1) {
        Color var2 = getColor(var1);
        if (var2 == null) {
            var2 = this.penColor;
        }

        long var3 = this.storeCurrentState(true, false, this.penWidth * 3.0D, var2, (Font)null, (String)null, 0, (Double)null);
        return var3;
    }

    public long dot(Color var1) {
        long var2 = this.storeCurrentState(true, false, this.penWidth * 3.0D, var1, (Font)null, (String)null, 0, (Double)null);
        return var2;
    }

    public long dot(String var1, double var2) {
        Color var4 = getColor(var1);
        if (var4 == null) {
            var4 = this.penColor;
        }

        long var5 = this.storeCurrentState(true, false, var2, var4, (Font)null, (String)null, 0, (Double)null);
        return var5;
    }

    public long dot(Color var1, double var2) {
        long var4 = this.storeCurrentState(true, false, var2, var1, (Font)null, (String)null, 0, (Double)null);
        return var4;
    }

    public long write(String var1, String var2, int var3, int var4, double var5, double var7) {
        return 0L;
    }

    public void undo(int var1) {
        for(int var2 = 0; var2 < var1; ++var2) {
            this.rollback();
        }

        lastUpdate = 0L;
        if (refreshMode != 2) {
            updateAll();
        }

    }

    public void undo() {
        this.rollback();
        lastUpdate = 0L;
        if (refreshMode != 2) {
            updateAll();
        }

    }

    public void redo(int var1) {
        for(int var2 = 0; var2 < var1; ++var2) {
            this.rollforward();
        }

        lastUpdate = 0L;
        if (refreshMode != 2) {
            updateAll();
        }

    }

    public void redo() {
        this.rollforward();
        lastUpdate = 0L;
        if (refreshMode != 2) {
            updateAll();
        }

    }

    public void clear() {
        synchronized(turtleLock) {
            long var2 = 0L;
            TreeMap var4 = (TreeMap)turtleStates.clone();
            Iterator var5 = var4.entrySet().iterator();

            while(var5.hasNext()) {
                Entry var6 = (Entry)var5.next();
                ArrayList var7 = (ArrayList)var6.getValue();
                long var8 = (Long)var6.getKey();
                if (getStateTurtle(var7) == this) {
                    if (var2 != 0L) {
                        turtleStates.remove(var2);
                    }

                    var2 = var8;
                }
            }

            redoStates.clear();
            restoreState(var2);
        }

        lastUpdate = 0L;
        if (refreshMode != 2) {
            updateAll();
        }

    }

    private void rollback() {
        int var1 = 0;
        synchronized(turtleLock) {
            long var3 = 0L;
            long var5 = 0L;
            Iterator var7 = turtleStates.descendingMap().entrySet().iterator();

            while(var7.hasNext()) {
                Entry var8 = (Entry)var7.next();
                ArrayList var9 = (ArrayList)var8.getValue();
                long var10 = (Long)var8.getKey();
                if (getStateTurtle(var9) == this) {
                    if (var1 != 0) {
                        var5 = var10;
                        break;
                    }

                    var3 = var10;
                    ++var1;
                }
            }

            if (var3 != 0L && var5 != 0L) {
                restoreState(var5);
                redoStates.put(var3, (ArrayList)turtleStates.remove(var3));
            }

        }
    }

    private void rollforward() {
        synchronized(turtleLock) {
            Iterator var2 = redoStates.entrySet().iterator();

            Entry var3;
            ArrayList var4;
            long var5;
            do {
                if (!var2.hasNext()) {
                    return;
                }

                var3 = (Entry)var2.next();
                var4 = (ArrayList)var3.getValue();
                var5 = (Long)var3.getKey();
            } while(getStateTurtle(var4) != this);

            turtleStates.put((Long)var3.getKey(), (ArrayList)redoStates.remove(var3.getKey()));
            restoreState(var5);
        }
    }

    public static void refreshMode(int var0) {
        refreshMode = var0;
        updateAll();
    }

    public static void backgroundMode(int var0) {
        backgroundMode = var0;
        updateAll();
    }

    public static void bgcolor(String var0) {
        Color var1 = getColor(var0);
        if (var1 != null) {
            backgroundColor = var1;
        }

        if (refreshMode != 2) {
            updateAll();
        }

    }

    public static void bgcolor(Color var0) {
        backgroundColor = var0;
        if (refreshMode != 2) {
            updateAll();
        }

    }

    public static void bgpic(String var0) {
        try {
            backgroundImage = ImageIO.read(new File(var0));
        } catch (Exception var2) {
            var2.printStackTrace();
        }

        if (refreshMode != 2) {
            updateAll();
        }

    }

    private static boolean addMouseBinding(String var0, Turtle var1, boolean var2, boolean var3, boolean var4) {
        String var5 = "";

        try {
            throw new Exception("Who called me?");
        } catch (Exception var16) {
            var5 = var16.getStackTrace()[2].getClassName();

            try {
                boolean var6 = false;
                Method[] var7 = Class.forName(var5).getDeclaredMethods();
                int var8 = var7.length;

                for(int var9 = 0; var9 < var8; ++var9) {
                    Method var10 = var7[var9];
                    if (var10.getName().equals(var0)) {
                        var6 = true;
                        Class[] var11 = var10.getParameterTypes();
                        int var12 = var11.length;

                        for(int var13 = 0; var13 < var12; ++var13) {
                            Class var14 = var11[var13];
                            if (!var14.getName().equals("double") && !var14.getName().equals("java.lang.Double") && !var14.getName().equals("Turtle")) {
                                var6 = false;
                                break;
                            }
                        }

                        if (var6) {
                            break;
                        }
                    }
                }

                if (!var6) {
                    System.out.println("ERROR");
                    return false;
                }
            } catch (Exception var15) {
                System.out.println("Calling Class not found.");
                return false;
            }

            if (!var2 || !mouseBindings.containsKey(var1)) {
                mouseBindings.put(var1, new ArrayList());
            }

            ArrayList var17 = new ArrayList();
            var17.add(var1);
            var17.add(var5);
            var17.add(var0);
            var17.add(var3);
            var17.add(var4);
            ((ArrayList)mouseBindings.get(var1)).add(var17);
            return true;
        }
    }

    private boolean addKeyBinding(String var1, String var2, boolean var3, boolean var4) {
        var2 = var2.toLowerCase();
        String var5 = "";

        try {
            throw new Exception("Who called me?");
        } catch (Exception var16) {
            var5 = var16.getStackTrace()[2].getClassName();

            try {
                boolean var6 = false;
                Method[] var7 = Class.forName(var5).getDeclaredMethods();
                int var8 = var7.length;

                for(int var9 = 0; var9 < var8; ++var9) {
                    Method var10 = var7[var9];
                    if (var10.getName().equals(var1)) {
                        var6 = true;
                        Class[] var11 = var10.getParameterTypes();
                        int var12 = var11.length;

                        for(int var13 = 0; var13 < var12; ++var13) {
                            Class var14 = var11[var13];
                            if (!var14.getName().equals("java.lang.String") && !var14.getName().equals("Turtle")) {
                                var6 = false;
                                break;
                            }
                        }

                        if (var6) {
                            break;
                        }
                    }
                }

                if (!var6) {
                    System.out.println("ERROR");
                    return false;
                }
            } catch (Exception var15) {
                System.out.println("Calling Class not found.");
                return false;
            }

            if (!var3 || !keyBindings.containsKey(var2)) {
                keyBindings.put(var2, new ArrayList());
            }

            ArrayList var17 = new ArrayList();
            var17.add(this);
            var17.add(var5);
            var17.add(var1);
            var17.add(var4);
            ((ArrayList)keyBindings.get(var2)).add(var17);
            return true;
        }
    }

    public boolean onKey(String var1, String var2) {
        return this.addKeyBinding(var1, var2, false, false);
    }

    public boolean onKey(String var1, String var2, boolean var3) {
        return this.addKeyBinding(var1, var2, var3, false);
    }

    public boolean onKey(String var1, String var2, boolean var3, boolean var4) {
        return this.addKeyBinding(var1, var2, var3, var4);
    }

    public static void zoom(double var0, double var2, double var4, double var6) {
        synchronized(turtleLock) {
            centerX = (var0 + var4) / 2.0D;
            centerY = (var2 + var6) / 2.0D;
            if ((double)width / (var4 - var0) > (double)height / (var6 - var2)) {
                scale = (double)height / (var6 - var2);
            } else {
                scale = (double)width / (var4 - var0);
            }

            updateAll();
        }
    }

    public static void zoomFit() {
        synchronized(turtleLock) {
            if (!turtleStates.isEmpty()) {
                Double var1 = getStateLocation((ArrayList)turtleStates.firstEntry().getValue());
                double var2 = var1.x;
                double var4 = var1.y;
                double var6 = var2;
                double var8 = var4;
                double var10 = 0.0D;
                double var12 = 0.0D;
                long var14 = System.nanoTime();
                if (refreshMode != 0) {
                    var14 = (Long)turtleStates.lastKey() + 1L;
                }

                Iterator var16 = turtleStates.headMap(var14).entrySet().iterator();

                while(var16.hasNext()) {
                    Entry var17 = (Entry)var16.next();
                    ArrayList var18 = (ArrayList)var17.getValue();
                    if (getStateIsPenDown(var18)) {
                        Double var19 = getStateLocation(var18);
                        if (var19.x < var2) {
                            var2 = var19.x;
                        }

                        if (var19.x > var6) {
                            var6 = var19.x;
                        }

                        if (var19.y < var4) {
                            var4 = var19.y;
                        }

                        if (var19.y > var8) {
                            var8 = var19.y;
                        }

                        var10 = getStateShapeWidth(var18);
                        var12 = getStateShapeHeight(var18);
                    }
                }

                double var29;
                if ((Long)turtleStates.lastKey() > var14 && getStateSpeed((ArrayList)turtleStates.lastEntry().getValue()) > 0.0D) {
                    var29 = 1.0D - (double)((Long)turtleStates.lastKey() - var14) / getStateSpeed((ArrayList)turtleStates.lastEntry().getValue()) / 1000000.0D;
                    Turtle var30 = getStateTurtle((ArrayList)turtleStates.lastEntry().getValue());
                    double var31 = var30._location.x;
                    double var21 = var30._location.y;
                    double var23 = var30.__location.x;
                    double var25 = var30.__location.y;
                    var31 += (var23 - var31) * var29;
                    var21 += (var25 - var21) * var29;
                    if (var31 < var2) {
                        var2 = var31;
                    }

                    if (var31 > var6) {
                        var6 = var31;
                    }

                    if (var21 < var4) {
                        var4 = var21;
                    }

                    if (var21 > var8) {
                        var8 = var21;
                    }
                }

                var29 = Math.max(var10, var12);
                zoom(var2 - var29 / 2.0D, var4 - var29 / 2.0D, var6 + var29 / 2.0D, var8 + var29 / 2.0D);
            }
        }
    }

    private static void updateAll() {
        lastUpdate = 0L;
        draw();
    }

    public static void update() {
        if (refreshMode == 2) {
            draw();
        }

    }

    private static void draw() {
        synchronized(turtleLock) {
            long var1 = System.nanoTime();
            if (turtleStates.isEmpty() || lastUpdate == 0L) {
                clearStorage();
                drawBackground(offscreen);
            }

            if (turtleStates.isEmpty()) {
                onscreen.drawImage(offscreenImage, 0, 0, (ImageObserver)null);
                window.repaint();
                if (applet != null) {
                    applet.repaint();
                }

            } else {
                if (refreshMode != 0) {
                    var1 = (Long)turtleStates.lastKey() + 1L;
                }

                Iterator var3;
                if (lastUpdate > (Long)turtleStates.lastKey()) {
                    midscreen.drawImage(offscreenImage, 0, 0, (ImageObserver)null);
                    var3 = turtles.iterator();

                    while(var3.hasNext()) {
                        Turtle var15 = (Turtle)var3.next();
                        if (var15.isVisible) {
                            var15.drawStamp(1.0D, midscreen);
                        }
                    }

                    onscreen.drawImage(midscreenImage, 0, 0, (ImageObserver)null);
                    window.repaint();
                    if (applet != null) {
                        applet.repaint();
                    }

                } else {
                    Turtle var5;
                    for(var3 = turtleStates.tailMap(lastUpdate).headMap(var1).entrySet().iterator(); var3.hasNext(); var5.drawDot(1.0D, offscreen)) {
                        Entry var4 = (Entry)var3.next();
                        retrieveState((Long)var4.getKey());
                        var5 = getStateTurtle((ArrayList)var4.getValue());
                        var5.drawLine(1.0D, offscreen);
                        if (var5._isStamp) {
                            var5.drawStamp(1.0D, offscreen);
                        }
                    }

                    midscreen.drawImage(offscreenImage, 0, 0, (ImageObserver)null);
                    Turtle var13 = null;
                    double var14 = 1.0D;
                    Long var6 = new Long(0L);
                    if (var1 < (Long)turtleStates.lastKey()) {
                        var13 = getStateTurtle((ArrayList)turtleStates.ceilingEntry(var1).getValue());
                        var6 = var13._time;
                        retrieveState((Long)turtleStates.ceilingKey(var1));
                        if (var13._speed > 0.0D) {
                            var14 = 1.0D - (double)((Long)turtleStates.ceilingKey(var1) - var1) / var13._speed / 1000000.0D;
                        } else {
                            var14 = 1.0D;
                        }

                        if (var14 < 0.0D) {
                            var14 = 0.0D;
                        }
                    }

                    Iterator var7 = turtles.iterator();

                    while(var7.hasNext()) {
                        Turtle var8 = (Turtle)var7.next();
                        if (var8 == var13) {
                            var8.drawLine(var14, midscreen);
                            if (var8._dotSize > 0.0D) {
                                var8.drawDot(var14, midscreen);
                            }

                            if (var8.isVisible) {
                                var8.drawStamp(var14, midscreen, false);
                            }

                            if (var8._isStamp) {
                                var8.drawStamp(var14, midscreen, true);
                            }

                            try {
                                retrieveState(var6);
                            } catch (Exception var11) {
                            }
                        } else if (var8.isVisible) {
                            var8.drawStamp(1.0D, midscreen);
                        }
                    }

                    lastUpdate = var1;
                    onscreen.drawImage(midscreenImage, 0, 0, (ImageObserver)null);
                    window.repaint();
                    if (applet != null) {
                        applet.repaint();
                    }

                }
            }
        }
    }

    private void drawLine(double var1, Graphics2D var3) {
        if (this._isPenDown) {
            var3.setColor(this._penColor);
            var3.setStroke(new BasicStroke((float)(scale * this._penWidth), 1, 1));
            if (this.__location != null && !this.__location.equals(this._location)) {
                double var4 = this._location.x;
                double var6 = this._location.y;
                double var8 = this.__location.x;
                double var10 = this.__location.y;
                if (var1 < 1.0D && var1 >= 0.0D) {
                    var4 = (var4 - var8) * var1 + var8;
                    var6 = (var6 - var10) * var1 + var10;
                }

                var3.drawLine((int)((var4 - centerX) * scale + (double)(width / 2)), (int)((var6 - centerY) * -scale + (double)(height / 2)), (int)((var8 - centerX) * scale + (double)(width / 2)), (int)((var10 - centerY) * -scale + (double)(height / 2)));
            }

        }
    }

    private void drawStamp(double var1, Graphics2D var3) {
        this.drawStamp(var1, var3, false);
    }

    private void drawStamp(double var1, Graphics2D var3, boolean var4) {
        if (this._location != null) {
            AffineTransform var5 = (AffineTransform)var3.getTransform().clone();
            AffineTransform var6 = var3.getTransform();
            double var7 = this._location.x;
            double var11 = this._location.y;
            double var15 = this._direction;
            double var19 = this._tilt;
            double var9;
            double var13;
            double var17;
            double var21;
            if (this.__location == null) {
                var9 = var7;
                var13 = var11;
                var17 = var15;
                var21 = var19;
            } else {
                var9 = this.__location.x;
                var13 = this.__location.y;
                var17 = this.__direction;
                var21 = this.__tilt;
            }

            if (var1 < 1.0D && var1 >= 0.0D) {
                var7 = (var7 - var9) * var1 + var9;
                var11 = (var11 - var13) * var1 + var13;
                var15 = (var15 - var17) * var1 + var17;
                var19 = (var19 - var21) * var1 + var21;
            }

            var6.translate((var7 - centerX) * scale + (double)(width / 2), (var11 - centerY) * -scale + (double)(height / 2));
            if (var4) {
                var6.scale(scale * var1, scale * var1);
            } else {
                var6.scale(scale, scale);
            }

            if (this._image == null) {
                var6.rotate(-Math.toRadians(var15 + var19));
                var6.scale(this._shapeWidth / 100.0D, this._shapeHeight / 100.0D);
                var6.translate(-50.0D, -50.0D);
                var3.setTransform(var6);
                Polygon var23 = (Polygon)shapes.get(this._shape);
                var3.setColor(this._fillColor);
                var3.fillPolygon(var23);
                var3.setColor(this._outlineColor);
                if (this._outlineWidth > 0.0D) {
                    var3.setStroke(new BasicStroke((float)(this._outlineWidth * scale), 1, 1));
                    var3.setTransform(var5);
                    GeneralPath var24 = new GeneralPath();
                    var24.append(var23.getPathIterator(var6), false);
                    var3.draw(var24);
                }
            } else {
                int var25 = this._image.getWidth();
                int var26 = this._image.getHeight();
                var6.rotate(-Math.toRadians(var15 + var19));
                var6.scale(this._shapeWidth / 1.0D / (double)var25, this._shapeHeight / 1.0D / (double)var26);
                var6.translate((double)(-var25 / 2), (double)(-var26 / 2));
                var3.setTransform(var6);
                var3.drawImage(this._image, 0, 0, (ImageObserver)null);
            }

            var3.setTransform(var5);
        }
    }

    private void drawDot(double var1, Graphics2D var3) {
        AffineTransform var4 = (AffineTransform)var3.getTransform().clone();
        AffineTransform var5 = var3.getTransform();
        var5.translate((this._location.x - centerX) * scale + (double)(width / 2), (this._location.y - centerY) * -scale + (double)(height / 2));
        var5.scale(scale * var1 / 2.0D, scale * var1 / 2.0D);
        var3.setTransform(var5);
        var3.setColor(this._dotColor);
        int var6 = (int)(this._dotSize * 1.0D);
        var3.fillOval(-var6, -var6, 2 * var6, 2 * var6);
        var3.setTransform(var4);
    }

    private static void drawBackground(Graphics2D var0) {
        var0.setColor(backgroundColor);
        var0.fillRect(0, 0, width, height);
        if (backgroundImage != null) {
            int var1 = backgroundImage.getWidth();
            int var2 = backgroundImage.getHeight();
            if (backgroundMode == 1) {
                offscreen.drawImage(backgroundImage, (width - var1) / 2, (height - var2) / 2, var1, var2, (ImageObserver)null);
            } else if (backgroundMode == 0) {
                offscreen.drawImage(backgroundImage, 0, 0, width, height, (ImageObserver)null);
            } else if (backgroundMode == 3) {
                offscreen.drawImage(backgroundImage, (int)(((double)(-var1 / 2) - centerX) * scale + (double)(width / 2)), (int)(((double)(var2 / 2) - centerY) * -scale + (double)(height / 2)), (int)((double)var1 * scale), (int)((double)var2 * scale), (ImageObserver)null);
            } else if (backgroundMode == 2) {
                for(int var3 = 0; var3 < width; var3 += var1) {
                    for(int var4 = 0; var4 < height; var4 += var2) {
                        offscreen.drawImage(backgroundImage, var3, var4, var1, var2, (ImageObserver)null);
                    }
                }
            } else if (backgroundMode == 4) {
                double var15 = centerX - (double)(width / 2) / scale;
                double var5 = centerY + (double)(height / 2) / scale;
                double var7 = centerX + (double)(width / 2) / scale;
                double var9 = centerY - (double)(height / 2) / scale;

                for(double var11 = (double)(((int)(var15 / (double)var1) - 1) * var1); var11 <= var7; var11 += (double)var1) {
                    for(double var13 = (double)((int)(var9 / (double)var2) * var2); var13 <= var5 + (double)var2; var13 += (double)var2) {
                        offscreen.drawImage(backgroundImage, (int)((var11 - centerX) * scale + (double)(width / 2)), (int)((var13 - centerY) * -scale + (double)(height / 2)), (int)Math.ceil((double)var1 * scale), (int)Math.ceil((double)var2 * scale), (ImageObserver)null);
                    }
                }
            }

        }
    }

    private void drawCrossHairs(double var1, Graphics2D var3) {
        if (this._location != null) {
            double var4 = (double)(System.nanoTime() / 100000000L) / 10.0D;
            AffineTransform var6 = (AffineTransform)var3.getTransform().clone();
            AffineTransform var7 = var3.getTransform();
            double var8 = this._location.x;
            double var12 = this._location.y;
            double var10;
            double var14;
            if (this.__location == null) {
                var10 = var8;
                var14 = var12;
            } else {
                var10 = this.__location.x;
                var14 = this.__location.y;
            }

            if (var1 < 1.0D && var1 >= 0.0D) {
                var8 = (var8 - var10) * var1 + var10;
                var12 = (var12 - var14) * var1 + var14;
            }

            var7.translate((var8 - centerX) * scale + (double)(width / 2), (var12 - centerY) * -scale + (double)(height / 2));
            byte var20 = 10;
            var7.scale(scale / (double)var20, scale / (double)var20);
            var3.setTransform(var7);
            byte var21 = 50;
            int var22 = (int)(Math.sqrt(this.shapeWidth * this.shapeWidth + this.shapeHeight * this.shapeHeight) * (double)var20 / 2.0D);
            var3.setColor(new Color(255, 255, 255));
            var3.setStroke(new BasicStroke((float)(6 * var20), 1, 1));
            var3.drawOval(-var22, -var22, 2 * var22, 2 * var22);
            var22 += var20;

            int var23;
            for(var23 = 0; var23 < 4; ++var23) {
                var3.drawLine((int)((double)var22 * Math.cos(1.5707963267948966D * (double)var23 + 6.283185307179586D * var4 / (double)var21)), (int)((double)var22 * Math.sin(1.5707963267948966D * (double)var23 + 6.283185307179586D * var4 / (double)var21)), (int)((double)(var22 + var22 / 5) * Math.cos(1.5707963267948966D * (double)var23 + 6.283185307179586D * var4 / (double)var21)), (int)((double)(var22 + var22 / 5) * Math.sin(1.5707963267948966D * (double)var23 + 6.283185307179586D * var4 / (double)var21)));
            }

            var22 -= var20;
            var3.setColor(new Color(0, 0, 0));
            var3.setStroke(new BasicStroke((float)(3 * var20), 1, 1));
            var3.drawOval(-var22, -var22, 2 * var22, 2 * var22);
            var22 += var20;

            for(var23 = 0; var23 < 4; ++var23) {
                var3.drawLine((int)((double)var22 * Math.cos(1.5707963267948966D * (double)var23 + 6.283185307179586D * var4 / (double)var21)), (int)((double)var22 * Math.sin(1.5707963267948966D * (double)var23 + 6.283185307179586D * var4 / (double)var21)), (int)((double)(var22 + var22 / 5) * Math.cos(1.5707963267948966D * (double)var23 + 6.283185307179586D * var4 / (double)var21)), (int)((double)(var22 + var22 / 5) * Math.sin(1.5707963267948966D * (double)var23 + 6.283185307179586D * var4 / (double)var21)));
            }

            var3.setTransform(var6);
        }
    }

    public static void setCanvasSize(int var0, int var1) {
        width = var0;
        height = var1;
        setupBuffering();
        window.pack();
        updateAll();
    }

    public static void save(String var0) {
        save(new File(var0));
    }

    private static void save(File var0) {
        WritableRaster var1 = onscreenImage.getRaster();
        WritableRaster var2 = var1.createWritableChild(0, 0, width, height, 0, 0, new int[]{0, 1, 2});
        DirectColorModel var3 = (DirectColorModel)onscreenImage.getColorModel();
        DirectColorModel var4 = new DirectColorModel(var3.getPixelSize(), var3.getRedMask(), var3.getGreenMask(), var3.getBlueMask());
        BufferedImage var5 = new BufferedImage(var4, var2, false, (Hashtable)null);

        try {
            String var6 = var0.getName().substring(var0.getName().lastIndexOf(46) + 1);
            if (!ImageIO.write(var5, var6, var0)) {
                throw new Exception("Didn't save file.");
            }
        } catch (Exception var7) {
            var0.delete();
            JOptionPane.showMessageDialog(window, "Sorry! We can not process your request at this time.", "Image Save Failed", 0);
        }

    }

    public static void main(String[] var0) {
        Turtle var1 = new Turtle();
        bgcolor("lightblue");
        var1.penColor("red");
        var1.width(10.0D);

        for(int var2 = 0; var2 < 200; ++var2) {
            var1.forward((double)var2 / 10.0D);
            var1.left(5.0D);
            if (var2 % 10 == 0) {
                var1.dot("orange");
            }
        }

    }

    public void actionPerformed(ActionEvent var1) {
        if (((JMenuItem)var1.getSource()).getText().equals("Save...")) {
            JFileChooser var2 = new JFileChooser(System.getProperty("user.dir"));
            var2.setFileFilter(new FileNameExtensionFilter("Image (*.jpg, *.jpeg, *.gif, *.bmp, *.png)", new String[]{"jpg", "png", "jpeg", "bmp", "gif"}));
            int var3 = var2.showSaveDialog(window);
            if (var3 == 0 && var2.getSelectedFile() != null) {
                File var4 = var2.getSelectedFile();
                save(var4);
            }
        }

    }

    public void mouseClicked(MouseEvent var1) {
        if (var1.getModifiers() == 8 && var1.getClickCount() == 2) {
            centerX = 0.0D;
            centerY = 0.0D;
            scale = 1.0D;
            updateAll();
        }

    }

    public void mouseEntered(MouseEvent var1) {
    }

    public void mouseExited(MouseEvent var1) {
    }

    public void mousePressed(MouseEvent var1) {
        dragx = var1.getX();
        dragy = var1.getY();
        modifiers += var1.getModifiers();
        synchronized(turtleLock) {
            Iterator var3 = turtles.iterator();

            while(var3.hasNext()) {
                Turtle var4 = (Turtle)var3.next();
                if (var4.contains(dragx, dragy)) {
                    var4.select();
                } else {
                    var4.unselect();
                }
            }

        }
    }

    public void mouseReleased(MouseEvent var1) {
        modifiers -= var1.getModifiers();
    }

    public void mouseDragged(MouseEvent var1) {
        modifiers = var1.getModifiers();
        if (var1.getModifiers() == 8) {
            x = var1.getX();
            int var2 = x - dragx;
            y = var1.getY();
            int var3 = y - dragy;
            dragx = x;
            dragy = y;
            synchronized(turtleLock) {
                centerX -= (double)var2 * 1.0D / scale;
                centerY += (double)var3 * 1.0D / scale;
            }

            updateAll();
        }

        x = var1.getX();
        y = var1.getY();
    }

    public void mouseMoved(MouseEvent var1) {
        modifiers = var1.getModifiers();
        x = var1.getX();
        y = var1.getY();
    }

    public void keyTyped(KeyEvent var1) {
    }

    public void keyPressed(KeyEvent var1) {
        String var2 = KeyEvent.getKeyText(var1.getKeyCode()).toLowerCase();
        synchronized(keyLock) {
            keysDown.add(var2);
            if (keyBindings.containsKey(var2)) {
                unprocessedKeys.add(var2);
            }

        }
    }

    public void keyReleased(KeyEvent var1) {
        String var2 = KeyEvent.getKeyText(var1.getKeyCode()).toLowerCase();
        synchronized(keyLock) {
            keysDown.remove(var2);
            processedKeys.remove(var2);
        }
    }

    private void processKeys() {
        new TreeSet();
        TreeSet var1;
        synchronized(keyLock) {
            var1 = (TreeSet)keysDown.clone();
        }

        var1.addAll(unprocessedKeys);
        Iterator var2 = var1.iterator();

        while(true) {
            String var3;
            do {
                if (!var2.hasNext()) {
                    return;
                }

                var3 = (String)var2.next();
            } while(!keyBindings.containsKey(var3));

            Iterator var4 = ((ArrayList)keyBindings.get(var3)).iterator();

            while(var4.hasNext()) {
                ArrayList var5 = (ArrayList)var4.next();
                Turtle var6 = (Turtle)var5.get(0);
                String var7 = (String)var5.get(1);
                String var8 = (String)var5.get(2);
                Boolean var9 = (Boolean)var5.get(3);
                if (!var9 && processedKeys.contains(var3)) {
                    break;
                }

                unprocessedKeys.remove(var3);
                processedKeys.add(var3);

                try {
                    Class var10 = Class.forName(var7);
                    Object var19 = var10.newInstance();
                    Method var21 = var19.getClass().getMethod(var8, var6.getClass());
                    var21.invoke(var19, var6);
                } catch (Exception var17) {
                    try {
                        Class var11 = Class.forName(var7);
                        Object var20 = var11.newInstance();
                        Method var22 = var20.getClass().getMethod(var8, var6.getClass(), var3.getClass());
                        var22.invoke(var20, var6, var3);
                    } catch (Exception var16) {
                        try {
                            Class var12 = Class.forName(var7);
                            Object var13 = var12.newInstance();
                            Method var14 = var13.getClass().getMethod(var8);
                            var14.invoke(var13);
                        } catch (Exception var15) {
                            System.out.println("KeyBinding for " + var3 + " has failed.");
                            var17.printStackTrace();
                            var16.printStackTrace();
                            var15.printStackTrace();
                        }
                    }
                }
            }
        }
    }

    public void componentHidden(ComponentEvent var1) {
    }

    public void componentMoved(ComponentEvent var1) {
    }

    public void componentResized(ComponentEvent var1) {
        width = (int)draw.getBounds().getWidth();
        height = (int)draw.getBounds().getHeight();
        setupBuffering();
        updateAll();
    }

    public void componentShown(ComponentEvent var1) {
    }

    public void mouseWheelMoved(MouseWheelEvent var1) {
        int var2 = var1.getWheelRotation();
        double var3 = Math.pow(1.1D, (double)var2);
        x = var1.getX();
        y = var1.getY();
        double var5 = (double)(width / 2 - x);
        double var7 = (double)(height / 2 - y);
        synchronized(turtleLock) {
            centerX -= (var5 * var3 - var5) / scale / var3;
            centerY += (var7 * var3 - var7) / scale / var3;
            scale *= var3;
        }

        updateAll();
    }

    public static String[] keysDown() {
        return (String[])keysDown.toArray(new String[0]);
    }

    public static boolean isKeyDown(String var0) {
        return keysDown.contains(var0);
    }

    public static int mouseX() {
        Turtle var10000 = turtle;
        return x;
    }

    public static int mouseY() {
        Turtle var10000 = turtle;
        return y;
    }

    public static boolean mouseButton() {
        return mouseButton1() || mouseButton2() || mouseButton3();
    }

    public static boolean mouseButton1() {
        Turtle var10000 = turtle;
        return (modifiers & 16) == 16;
    }

    public static boolean mouseButton2() {
        Turtle var10000 = turtle;
        return (modifiers & 8) == 8;
    }

    public static boolean mouseButton3() {
        Turtle var10000 = turtle;
        return (modifiers & 4) == 4;
    }

    public static double canvasX(double var0) {
        return (var0 - (double)width / 2.0D) / scale + centerX;
    }

    public static double canvasY(double var0) {
        return (-var0 + (double)height / 2.0D) / scale + centerY;
    }

    static {
        init();
    }
}
