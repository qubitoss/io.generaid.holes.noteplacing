package io.generaid.holes.noteplacing;

import io.generaid.holes.noteplacing.export.svg.SvgExporter;

import java.util.*;

public class App {
    public static void main(String[] args) {
        App app = new App();
        app.process();
    }

    public void process() {
        HolePanel hp = new HolePanel(Sources.fetchHolesAsMap(), Sources.fetchNotesAsMap());

        hp.placeNotes();
        listOverlapping(hp);

        SvgExporter.exportToSvg(hp);
    }

    private void listOverlapping(HolePanel hp) {
        List<String> res = new ArrayList<>();

        hp.getAllOverlappingAreas().forEach((n, a) -> {
            a.stream()
                    .map(e -> {
                        String[] overlapped = {e.tag()+":"+(e.getClass() == Hole.class ? "H" : "N"), n.tag()+":N"};
                        Arrays.sort(overlapped);
                        return overlapped[0] + " " + overlapped[1].intern();
                    })
                    .forEach(res::add);
        });

        HashSet<String> resRes = new HashSet<>(res);
        resRes.forEach(System.out::println);
        System.out.println("---------------------------------- " + resRes.size());
    }
}
