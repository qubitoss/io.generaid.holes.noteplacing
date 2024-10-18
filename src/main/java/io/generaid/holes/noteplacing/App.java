package io.generaid.holes.noteplacing;

import io.generaid.holes.noteplacing.export.svg.SvgExporter;

public class App {
    public static void main(String[] args) {
        App app = new App();
        app.process();
    }

    public void process() {
        HolePanel hp = new HolePanel(Sources.fetchHolesAsMap(), Sources.fetchNotesAsMap());
        hp.placeNotes();

        listOverlapping(hp);
        hp.placeNotesAgain();

        listOverlapping(hp);
        hp.placeNotesAgain();

//        listOverlapping(hp);
//        hp.placeNotesAgain();

//        hp.exportToSvg();
        SvgExporter.exportToSvg(hp);
    }


    private void listOverlapping(HolePanel hp) {
        hp.getAllOverlappingAreas().forEach((n, a) ->
                System.out.println(n.tag() + ": " + a.stream().map(e -> e.tag()+":"+(e.getClass()== Hole.class?"H":"N")).toList()));
        System.out.println("----------------------------------");
    }
}
