package org.jahia.modules.charts;

import net.htmlparser.jericho.*;

import java.util.Iterator;

/**
 * Created by IntelliJ IDEA.
 * User: Charles Flond
 * Date: 13 déc. 2010
 * Time: 11:43:00
 * To change this template use File | Settings | File Templates.
 */
public class HtmlToXmlTableConverter {

    public static String convert(String input) throws Exception {
        StringBuilder output = new StringBuilder("");

        Source source = new Source(input);
        Iterator<Element> iterator = source.getAllElements().iterator();

        boolean hasATable=false;
        String caption="",xAxisName="",yAxisName="";
        StringBuilder sets= new StringBuilder("");

        while(iterator.hasNext())
        {
            Element currentElement = iterator.next();

            if(currentElement.getStartTag().getName().equals("table"))
            {
                hasATable=true;
            }
            else if(currentElement.getStartTag().getName().equals("caption"))
            {
                caption=currentElement.getContent().toString().replace("\t","").replace("\n","");
            }
            else if(currentElement.getStartTag().getName().equals("th"))
            {
                if(xAxisName.isEmpty()) xAxisName=currentElement.getContent().toString().replace("\t","").replace("\n","");
                else if (yAxisName.isEmpty()) yAxisName=currentElement.getContent().toString().replace("\t","").replace("\n","");
                else throw new Exception("More than 2 columns is not yet supported by this module");
            }
            else if(currentElement.getStartTag().getName().equals("tr"))
            {
                   StringBuilder currentSet= new StringBuilder("");
                   Iterator<Element> currentRowIterator = currentElement.getContent().getAllElements().iterator();
                   int i=0;
                   while(currentRowIterator.hasNext())
                   {
                       ++i;
                       Element rowAttribute = currentRowIterator.next();
                       if(rowAttribute.getStartTag().getName().equals("td"))
                       {
                           if(i==1) currentSet.append(" name='"+rowAttribute.getContent().toString().replace("\t","").replace("\n","")+"'");
                           else if(i==2) currentSet.append(" value='"+rowAttribute.getContent().toString().replace("\t","").replace("\n","")+"'");
                           else throw new Exception("More than 2 columns is not yet supported by this module");
                       }
                   }
                   if(currentSet.length()>0) sets.append("<set" + currentSet + " />");
            }

        }

        if(hasATable)
        {
            output.append("<graph");
            if(!caption.isEmpty()) output.append(" caption='"+caption+"'");
            if(!xAxisName.isEmpty()) output.append(" xAxisName='"+xAxisName+"'");
            if(!yAxisName.isEmpty()) output.append(" yAxisName='"+yAxisName+"'");
            output.append(">");

            //Output of rows
            output.append(sets);

            output.append("</graph>");
        }

        return output.toString();
    }
    
}
