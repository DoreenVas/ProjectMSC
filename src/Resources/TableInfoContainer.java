package Resources;

import GUI.MainWindow;

import java.util.ArrayList;
import java.util.Arrays;

/*****
 * Table info container class.
 * The class holds all info of a single row in the results table.
 */
public class TableInfoContainer {
    // members
    private static String[] titles = {"סוג משחק", "מגבלת הזמן", "מספר התמונות שזוהו", "תאריך המשחק", "יד דומיננטית"};
    private static String[] shapesColumns = {"arrow", "rectangle", "diamond", "pie", "triangle", "heart", "flower",
            "hexagon", "moon", "plus", "oval", "two_triangles", "circle", "star"};
    private static String[] texturesColumns = {"four_dots", "waves", "arrow_head", "strips", "happy_smiley", "spikes"
            , "dollar", "net", "note", "arcs", "monitor", "sad_smiley", "strudel", "four_bubbles", "spiral", "squares"};

    private String gameType;
    private String timeLimit;
    private String numOfRecognizedButtons;
    private String gameDate;
    private String dominantHand;
    private String arrow;
    private String rectangle;
    private String diamond;
    private String pie;
    private String triangle;
    private String heart;
    private String flower;
    private String hexagon;
    private String moon;
    private String plus;
    private String oval;
    private String two_triangles;
    private String circle;
    private String star;
    private String four_dots;
    private String waves;
    private String arrow_head;
    private String strips;
    private String happy_smiley;
    private String spikes;
    private String dollar;
    private String net;
    private String note;
    private String arcs;
    private String monitor;
    private String sad_smiley;
    private String strudel;
    private String four_bubbles;
    private String spiral;
    private String squares;

    /******
     * Set the game type according to the current language of the window
     * @param gameType the type of the game (Shapes, Textures or Both)
     * @return the same tableInfoContainer
     */
    public TableInfoContainer setGameType(String gameType) {
        if (MainWindow.language.equals("Hebrew")) {
            switch (gameType) {
                case "Shapes":
                    this.gameType =  "צורות";
                    break;
                case "Textures":
                    this.gameType =  "מרקמים";
                    break;
                case "Both":
                    this.gameType =  "משולב";
                    break;
                default:
                    this.gameType =  gameType;
                    break;
            }
        } else {
            switch (gameType) {
                case "צורות":
                    this.gameType =  "Shapes";
                    break;
                case "מרקמים":
                    this.gameType =  "Textures";
                    break;
                case "משולב":
                    this.gameType =  "Both";
                    break;
                default:
                    this.gameType =  gameType;
                    break;
            }
        }
        return this;
    }

    public TableInfoContainer setTimeLimit(String timeLimit) {
        this.timeLimit = timeLimit;
        return this;
    }

    public TableInfoContainer setNumOfRecognizedButtons(String numOfRecognizedButtons) {
        this.numOfRecognizedButtons = numOfRecognizedButtons;
        return this;
    }

    public TableInfoContainer setGameDate(String gameDate) {
        this.gameDate = gameDate;
        return this;
    }

    public TableInfoContainer setDominantHand(String dominantHand) {
        if (dominantHand == "null") {
            this.dominantHand = "-";
        } else {
            this.dominantHand = dominantHand;
        }
        return this;
    }

    public TableInfoContainer setArrow(String arrow) {
        if (arrow == "null") {
            this.arrow = "-";
        } else {
            this.arrow = arrow;
        }
        return this;
    }

    public TableInfoContainer setRectangle(String rectangle) {
        if (rectangle == "null") {
            this.rectangle = "-";
        } else {
            this.rectangle = rectangle;
        }
        return this;
    }

    public TableInfoContainer setDiamond(String diamond) {
        if (diamond == "null") {
            this.diamond = "-";
        } else {
            this.diamond = diamond;
        }
        return this;
    }

    public TableInfoContainer setPie(String pie) {
        if (pie == "null") {
            this.pie = "-";
        } else {
            this.pie = pie;
        }
        return this;
    }

    public TableInfoContainer setTriangle(String triangle) {
        if (triangle == "null") {
            this.triangle = "-";
        } else {
            this.triangle = triangle;
        }
        return this;
    }

    public TableInfoContainer setHeart(String heart) {
        if (heart == "null") {
            this.heart = "-";
        } else {
            this.heart = heart;
        }
        return this;
    }

    public TableInfoContainer setFlower(String flower) {
        if (flower == "null") {
            this.flower = "-";
        } else {
            this.flower = flower;
        }
        return this;
    }

    public TableInfoContainer setHexagon(String hexagon) {
        if (hexagon == "null") {
            this.hexagon = "-";
        } else {
            this.hexagon = hexagon;
        }
        return this;
    }

    public TableInfoContainer setMoon(String moon) {
        if (moon == "null") {
            this.moon = "-";
        } else {
            this.moon = moon;
        }
        return this;
    }

    public TableInfoContainer setPlus(String plus) {
        if (plus == "null") {
            this.plus = "-";
        } else {
            this.plus = plus;
        }
        return this;
    }

    public TableInfoContainer setOval(String oval) {
        if (oval == "null") {
            this.oval = "-";
        } else {
            this.oval = oval;
        }
        return this;
    }

    public TableInfoContainer setTwo_triangles(String two_triangles) {
        if (two_triangles == "null") {
            this.two_triangles = "-";
        } else {
            this.two_triangles = two_triangles;
        }
        return this;
    }

    public TableInfoContainer setCircle(String circle) {
        if (circle == "null") {
            this.circle = "-";
        } else {
            this.circle = circle;
        }
        return this;
    }

    public TableInfoContainer setStar(String star) {
        if (star == "null") {
            this.star = "-";
        } else {
            this.star = star;
        }
        return this;
    }

    public TableInfoContainer setFour_dots(String four_dots) {
        if (four_dots == "null") {
            this.four_dots = "-";
        } else {
            this.four_dots = four_dots;
        }
        return this;
    }

    public TableInfoContainer setWaves(String waves) {
        if (waves == "null") {
            this.waves = "-";
        } else {
            this.waves = waves;
        }
        return this;
    }

    public TableInfoContainer setArrow_head(String arrow_head) {
        if (arrow_head == "null") {
            this.arrow_head = "-";
        } else {
            this.arrow_head = arrow_head;
        }
        return this;
    }

    public TableInfoContainer setStrips(String strips) {
        if (strips == "null") {
            this.strips = "-";
        } else {
            this.strips = strips;
        }
        return this;
    }

    public TableInfoContainer setHappy_smiley(String happy_smiley) {
        if (happy_smiley == "null") {
            this.happy_smiley = "-";
        } else {
            this.happy_smiley = happy_smiley;
        }
        return this;
    }

    public TableInfoContainer setSpikes(String spikes) {
        if (spikes == "null") {
            this.spikes = "-";
        } else {
            this.spikes = spikes;
        }
        return this;
    }

    public TableInfoContainer setDollar(String dollar) {
        if (dollar == "null") {
            this.dollar = "-";
        } else {
            this.dollar = dollar;
        }
        return this;
    }

    public TableInfoContainer setNet(String net) {
        if (net == "null") {
            this.net = "-";
        } else {
            this.net = net;
        }
        return this;
    }

    public TableInfoContainer setNote(String note) {
        if (note == "null") {
            this.note = "-";
        } else {
            this.note = note;
        }
        return this;
    }

    public TableInfoContainer setArcs(String arcs) {
        if (arcs == "null") {
            this.arcs = "-";
        } else {
            this.arcs = arcs;
        }
        return this;
    }

    public TableInfoContainer setMonitor(String monitor) {
        if (monitor == "null") {
            this.monitor = "-";
        } else {
            this.monitor = monitor;
        }
        return this;
    }

    public TableInfoContainer setSad_smiley(String sad_smiley) {
        if (sad_smiley == "null") {
            this.sad_smiley = "-";
        } else {
            this.sad_smiley = sad_smiley;
        }
        return this;
    }

    public TableInfoContainer setStrudel(String strudel) {
        if (strudel == "null") {
            this.strudel = "-";
        } else {
            this.strudel = strudel;
        }
        return this;
    }

    public TableInfoContainer setFour_bubbles(String four_bubbles) {
        if (four_bubbles == "null") {
            this.four_bubbles = "-";
        } else {
            this.four_bubbles = four_bubbles;
        }
        return this;
    }

    public TableInfoContainer setSpiral(String spiral) {
        if (spiral == "null") {
            this.spiral = "-";
        } else {
            this.spiral = spiral;
        }
        return this;
    }

    public TableInfoContainer setSquares(String squares) {
        if (squares == "null") {
            this.squares = "-";
        } else {
            this.squares = squares;
        }
        return this;
    }


    public String getGameType() {
        return gameType;
    }

    public String getTimeLimit() {
        return timeLimit;
    }

    public String getNumOfRecognizedButtons() {
        return numOfRecognizedButtons;
    }

    public String getGameDate() {
        return gameDate;
    }

    public String getDominantHand() {
        return dominantHand;
    }

    public String getArrow() {
        return arrow;
    }

    public String getRectangle() {
        return rectangle;
    }

    public String getDiamond() {
        return diamond;
    }

    public String getPie() {
        return pie;
    }

    public String getTriangle() {
        return triangle;
    }

    public String getHeart() {
        return heart;
    }

    public String getFlower() {
        return flower;
    }

    public String getHexagon() {
        return hexagon;
    }

    public String getMoon() {
        return moon;
    }

    public String getPlus() {
        return plus;
    }

    public String getOval() {
        return oval;
    }

    public String getTwo_triangles() {
        return two_triangles;
    }

    public String getCircle() {
        return circle;
    }

    public String getStar() {
        return star;
    }

    public String getFour_dots() {
        return four_dots;
    }

    public String getWaves() {
        return waves;
    }

    public String getArrow_head() {
        return arrow_head;
    }

    public String getStrips() {
        return strips;
    }

    public String getHappy_smiley() {
        return happy_smiley;
    }

    public String getSpikes() {
        return spikes;
    }

    public String getDollar() {
        return dollar;
    }

    public String getNet() {
        return net;
    }

    public String getNote() {
        return note;
    }

    public String getArcs() {
        return arcs;
    }

    public String getMonitor() {
        return monitor;
    }

    public String getSad_smiley() {
        return sad_smiley;
    }

    public String getStrudel() {
        return strudel;
    }

    public String getFour_bubbles() {
        return four_bubbles;
    }

    public String getSpiral() {
        return spiral;
    }

    public String getSquares() {
        return squares;
    }

    public ArrayList<String> getValues() {
        ArrayList<String> values = new ArrayList<>();
        values.add(gameType);
        values.add(timeLimit);
        values.add(numOfRecognizedButtons);
        values.add(gameDate);
        values.add(dominantHand);
        values.add(arrow);
        values.add(rectangle);
        values.add(diamond);
        values.add(pie);
        values.add(triangle);
        values.add(heart);
        values.add(flower);
        values.add(hexagon);
        values.add(moon);
        values.add(plus);
        values.add(oval);
        values.add(two_triangles);
        values.add(circle);
        values.add(star);
        values.add(four_dots);
        values.add(waves);
        values.add(arrow_head);
        values.add(strips);
        values.add(happy_smiley);
        values.add(spikes);
        values.add(dollar);
        values.add(net);
        values.add(note);
        values.add(arcs);
        values.add(monitor);
        values.add(sad_smiley);
        values.add(strudel);
        values.add(four_bubbles);
        values.add(spiral);
        values.add(squares);
        return values;
    }

    public ArrayList<String> getTitles() {
        ArrayList<String> values = new ArrayList<>();
        String[] temp_titles = null;
        switch (MainWindow.language) {
            case "Hebrew":
                temp_titles = new String[]{"סוג משחק", "מגבלת הזמן", "מספר התמונות שזוהו", "תאריך המשחק", "יד דומיננטית"};
                break;
            case "English":
                temp_titles = new String[]{"Game type", "Time limit", "Number of Correct Images", "Game date", "Dominant hand"};
                break;
        }
        values.addAll(Arrays.asList(temp_titles));
        values.addAll(Arrays.asList(shapesColumns));
        values.addAll(Arrays.asList(texturesColumns));
        return values;
    }

    /******
     * Returns a list with all the shapes titles.
     * @return a list with all shapes titles.
     */
    public ArrayList<String> getShapesTitles() {
        ArrayList<String> values = new ArrayList<>();
        values.addAll(Arrays.asList(shapesColumns));
        return values;
    }

    /******
     * Returns a list with all the textures titles.
     * @return a list with all textures titles.
     */
    public ArrayList<String> getTexturesTitles() {
        ArrayList<String> values = new ArrayList<>();
        values.addAll(Arrays.asList(texturesColumns));
        return values;
    }
}
