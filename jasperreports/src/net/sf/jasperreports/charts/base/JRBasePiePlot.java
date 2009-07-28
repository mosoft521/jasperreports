/*
 * ============================================================================
 * GNU Lesser General Public License
 * ============================================================================
 *
 * JasperReports - Free Java report-generating library.
 * Copyright (C) 2001-2009 JasperSoft Corporation http://www.jaspersoft.com
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307, USA.
 * 
 * JasperSoft Corporation
 * 539 Bryant Street, Suite 100
 * San Francisco, CA 94107
 * http://www.jaspersoft.com
 */
package net.sf.jasperreports.charts.base;

import java.io.IOException;
import java.io.ObjectInputStream;

import net.sf.jasperreports.charts.JRItemLabel;
import net.sf.jasperreports.charts.JRPiePlot;
import net.sf.jasperreports.engine.JRChart;
import net.sf.jasperreports.engine.JRChartPlot;
import net.sf.jasperreports.engine.JRConstants;
import net.sf.jasperreports.engine.JRExpressionCollector;
import net.sf.jasperreports.engine.base.JRBaseChartPlot;
import net.sf.jasperreports.engine.base.JRBaseObjectFactory;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public class JRBasePiePlot extends JRBaseChartPlot implements JRPiePlot
{
	private static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;
	
	public static final String PROPERTY_CIRCULAR = "circular";

	public static final String PROPERTY_LABEL_FORMAT = "labelFormat";
	public static final String PROPERTY_LEGEND_LABEL_FORMAT = "legendLabelFormat";
	public static final String PROPERTY_ITEM_LABEL = "itemLabel";

	/*
	 * README
	 * 
	 * Make sure that all fields are read in readObject().
	 */
	protected Boolean circular = null;
	protected String labelFormat = null;
	protected String legendLabelFormat = null;
	
	protected JRItemLabel itemLabel = null;

	/**
	 *
	 */
	public JRBasePiePlot(JRChartPlot piePlot, JRChart chart)
	{
		super(piePlot, chart);
		if (piePlot == null)
		{
			itemLabel = new JRBaseItemLabel(null, chart);
		}
		else
		{
			itemLabel = new JRBaseItemLabel(((JRPiePlot)piePlot).getItemLabel(), chart);
		}
		
	}


	/**
	 *
	 */
	public JRBasePiePlot(JRPiePlot piePlot, JRBaseObjectFactory factory)
	{
		super(piePlot, factory);
		circular = piePlot.getCircular();
		labelFormat = piePlot.getLabelFormat();
		legendLabelFormat = piePlot.getLegendLabelFormat();
		itemLabel = new JRBaseItemLabel(piePlot.getItemLabel(), factory);
	}
	
	/**
	 *
	 */
	public void collectExpressions(JRExpressionCollector collector)
	{
	}


	/**
	 * @deprecated Replaced by {@link #getCircular()}
	 */
	public boolean isCircular() {
		return circular == null ? true : circular.booleanValue();
	}

	/**
	 * @return the circular
	 */
	public Boolean getCircular() {
		return circular;
	}

	/**
	 * @param isCircular the isCircular to set
	 */
	public void setCircular(boolean isCircular) {
		setCircular(Boolean.valueOf(isCircular));
	}

	/**
	 * @param isCircular the isCircular to set
	 */
	public void setCircular(Boolean isCircular) {
		Boolean old = this.circular;
		this.circular = isCircular;
		getEventSupport().firePropertyChange(PROPERTY_CIRCULAR, old, this.circular);
	}


	/**
	 * @return the labelFormat
	 */
	public String getLabelFormat() {
		return labelFormat;
	}


	/**
	 * @param labelFormat the labelFormat to set
	 */
	public void setLabelFormat(String labelFormat) {
		String old = this.labelFormat;
		this.labelFormat = labelFormat;
		getEventSupport().firePropertyChange(PROPERTY_LABEL_FORMAT, old, this.labelFormat);
	}

	
	/**
	 * @return the legendLabelFormat
	 */
	public String getLegendLabelFormat() {
		return legendLabelFormat;
	}

	/**
	 *
	 */
	public JRItemLabel getItemLabel()
	{
		return itemLabel;
	}
	
	/**
	 * @param legendLabelFormat the legendLabelFormat to set
	 */
	public void setLegendLabelFormat(String legendLabelFormat) {
		String old = this.legendLabelFormat;
		this.legendLabelFormat = legendLabelFormat;
		getEventSupport().firePropertyChange(PROPERTY_LEGEND_LABEL_FORMAT, old, this.legendLabelFormat);
	}

	/**
	 * @param itemLabel the itemLabel to set
	 */
	public void setItemLabel(JRItemLabel itemLabel) {
		JRItemLabel old = this.itemLabel;
		this.itemLabel = itemLabel;
		getEventSupport().firePropertyChange(PROPERTY_ITEM_LABEL, old, this.itemLabel);
	}

	/**
	 *
	 */
	public Object clone(JRChart parentChart) 
	{
		JRBasePiePlot clone = (JRBasePiePlot)super.clone(parentChart);
		if (itemLabel != null)
		{
			clone.itemLabel = (JRItemLabel)itemLabel.clone();
		}
		
		return clone;
	}

	/**
	 * This field is only for serialization backward compatibility.
	 */
	private int PSEUDO_SERIAL_VERSION_UID = JRConstants.PSEUDO_SERIAL_VERSION_UID_3_1_3;
	//we need this field for fields.get("isCircular")
	private boolean isCircular;
	
	private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException
	{
		// this fixes a problem with JFreeChart that changed the default value of isCircular at some point.
		// look into SVN history for details
		ObjectInputStream.GetField fields = in.readFields();
		//the following lines are required because above we called readFields(), not defaultReadObject()
		labelFormat = (String) fields.get("labelFormat", null);
		legendLabelFormat = (String) fields.get("legendLabelFormat", null);
		itemLabel = (JRItemLabel) fields.get("itemLabel", null);
		
		PSEUDO_SERIAL_VERSION_UID = fields.get("PSEUDO_SERIAL_VERSION_UID", 0);
		if (PSEUDO_SERIAL_VERSION_UID < JRConstants.PSEUDO_SERIAL_VERSION_UID_3_1_3)
		{
			boolean circularField = fields.get("isCircular", true);
			circular = Boolean.valueOf(circularField);
		}
		else
		{
			circular = (Boolean) fields.get("circular", null);
		}
	}
	
}
