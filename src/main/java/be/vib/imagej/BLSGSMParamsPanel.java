package be.vib.imagej;

import java.text.NumberFormat;

import javax.swing.BorderFactory;
import javax.swing.GroupLayout;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JSlider;
import javax.swing.JSpinner;

class BLSGSMParamsPanel extends DenoiseParamsPanelBase 
{
	private BLSGSMParams params;
	private SliderFieldPair sigmaPair;
	private SliderSpinnerPair scalesPair;
	
	public BLSGSMParamsPanel(BLSGSMParams params)
	{
		this.params = params;
		buildUI();
	}
	
	private void buildUI()
	{
		setBorder(BorderFactory.createTitledBorder("BLS-GSM Denoising Parameters"));
		
		NumberFormat floatFormat = NumberFormat.getNumberInstance();
		floatFormat.setMinimumFractionDigits(2);
		
		sigmaPair = new SliderFieldPair(0, 100, floatFormat, params.sigmaMin, params.sigmaMax);
		sigmaPair.setValue(params.sigma);
		sigmaPair.addPropertyChangeListener(e -> { params.sigma = sigmaPair.getValue(); fireParamsChangeEvent(); });
		
		JSlider sigmaSlider = sigmaPair.getSlider();
		
		JFormattedTextField sigmaField = sigmaPair.getFloatField();
		sigmaField.setColumns(5);
		
		JLabel sigmaLabel = new JLabel("Sigma:");
		sigmaLabel.setToolTipText("Should be set to the noise standard deviation for best results.");

		//
		
		scalesPair = new SliderSpinnerPair(BLSGSMParams.scalesMin, BLSGSMParams.scalesMax);
		scalesPair.setValue(params.scales);
		scalesPair.addPropertyChangeListener(e -> { params.scales = scalesPair.getValue(); fireParamsChangeEvent(); });
		
		JSlider scalesSlider = scalesPair.getSlider();
		
		JSpinner scalesSpinner = scalesPair.getSpinner();
		
		JLabel scalesLabel = new JLabel("Analysis scales:");
		scalesLabel.setToolTipText("A larger value models the wavelet frequency scales better.");

		//
		
		GroupLayout layout = new GroupLayout(this);
		layout.setAutoCreateGaps(true);
		layout.setAutoCreateContainerGaps(true);
				
		layout.setHorizontalGroup(
		   layout.createSequentialGroup()
		      .addGroup(layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
			           .addComponent(sigmaLabel)
			           .addComponent(scalesLabel))
		      .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
			           .addComponent(sigmaField)
			           .addComponent(scalesSpinner))
		      .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
			           .addComponent(sigmaSlider)
			           .addComponent(scalesSlider))
		);
		
		layout.setVerticalGroup(
		   layout.createSequentialGroup()
		      .addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
		    		   .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
		    				  .addComponent(sigmaLabel)
		    				  .addComponent(sigmaField))
			           .addComponent(sigmaSlider))
		      .addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
		    		   .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
		    				  .addComponent(scalesLabel)
		    				  .addComponent(scalesSpinner))
			           .addComponent(scalesSlider))
		);    	
		
		setLayout(layout);
	}
	
	@Override
	public void updatePanelFromParams()
	{
		sigmaPair.updateRange(params.sigmaMin, params.sigmaMax, params.sigma);
		scalesPair.updateRange(BLSGSMParams.scalesMin, BLSGSMParams.scalesMax, params.scales);
	}
}