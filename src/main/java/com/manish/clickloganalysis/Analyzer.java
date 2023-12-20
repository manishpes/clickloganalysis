package com.manish.clickloganalysis;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import tech.tablesaw.api.Table;
import tech.tablesaw.plotly.Plot;
import tech.tablesaw.plotly.api.Histogram;
import tech.tablesaw.plotly.components.Figure;
import tech.tablesaw.plotly.components.Marker;
import tech.tablesaw.plotly.components.Symbol;
import tech.tablesaw.plotly.traces.ScatterTrace;

public class Analyzer {
	public static void main (String args[]) throws IOException {

		Scanner sc = new Scanner(System.in);
		System.out.print("Enter the full file name : ");
		String filePath = sc.nextLine();
		File file = new File(filePath);
		if(file == null || !file.exists() && !file.canRead()) {
			System.out.print("Invalid file Path");
			return;
		}
		Table table = Table.read().csv(filePath);
		//System.out.println(table.structure());
		Predicate<String> isValid = new  Predicate<String>() {
			public boolean test(String t) {
				Pattern p = Pattern.compile("\\b[0-9a-zA-Z]{8}\\b-[0-9a-zA-Z]{4}-[0-9a-zA-Z]{4}-[0-9a-zA-Z]{4}-\\b[0-9a-zA-Z]{12}\\b");
				Matcher m = p.matcher(t);
				if (m.find()) {
					return true;
				}else {
					return false;
				}
			}
		};
		System.out.println("\n 1.b Valid google aid "+ table.textColumn("google_aid").filter(isValid).size());
		System.out.println("\n 1.b Valid ios aid "+table.stringColumn("ios_ifa").filter(isValid).size());
		
		Table result = table.countBy(table.intColumn("affiliate_id")).sortAscendingOn("Category");		
		Plot.show(Histogram.create("2.2 no of clicks v/s affliateid ", table, "affiliate_id"));
		
	    ScatterTrace trace1 =
	            ScatterTrace.builder(result.categoricalColumn(0), result.numberColumn(1))
	                .mode(ScatterTrace.Mode.LINE_TEXT_AND_MARKERS)
	                .marker(
	                    Marker.builder()
	                        .colorScale(Marker.Palette.CIVIDIS)
	                        .opacity(.5)
	                        .showScale(false)
	                        .symbol(Symbol.CIRCLE_CROSS)
	                        .build())
	                .build();

	        Plot.show(new Figure(trace1));
		}
	}

