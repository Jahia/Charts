/**
 * This file is part of Jahia, next-generation open source CMS:
 * Jahia's next-generation, open source CMS stems from a widely acknowledged vision
 * of enterprise application convergence - web, search, document, social and portal -
 * unified by the simplicity of web content management.
 *
 * For more information, please visit http://www.jahia.com.
 *
 * Copyright (C) 2002-2011 Jahia Solutions Group SA. All rights reserved.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301, USA.
 *
 * As a special exception to the terms and conditions of version 2.0 of
 * the GPL (or any later version), you may redistribute this Program in connection
 * with Free/Libre and Open Source Software ("FLOSS") applications as described
 * in Jahia's FLOSS exception. You should have received a copy of the text
 * describing the FLOSS exception, and it is also available here:
 * http://www.jahia.com/license
 *
 * Commercial and Supported Versions of the program (dual licensing):
 * alternatively, commercial and supported versions of the program may be used
 * in accordance with the terms and conditions contained in a separate
 * written agreement between you and Jahia Solutions Group SA.
 *
 * If you are unsure which license is appropriate for your use,
 * please contact the sales department at sales@jahia.com.
 */

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
