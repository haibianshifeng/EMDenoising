package be.vib.imagej;

import be.vib.bits.QFunction;
import be.vib.bits.QUtils;
import be.vib.bits.QValue;

class WaveletThresholdingDenoiser extends Denoiser
{
	final WaveletThresholdingParams params;
	
	public WaveletThresholdingDenoiser(LinearImage image, WaveletThresholdingParams params)
	{
		super(image);
		this.params = params;
	}
	
	@Override
	public byte[] call()
	{		
		QFunction waveletThresholding = loadDenoiseFunction("E:\\git\\DenoisingIJ2Repository\\DenoisingIJ2\\src\\main\\resources\\quasar\\wavelet_thresholding.q",
                                                            "wav_denoise(mat,scalar,int,mat,mat,string,scalar)");
				
		QValue imageCube = QUtils.newCubeFromGrayscaleArray(image.width, image.height, image.pixels);
		
		QValue w1 = QValue.readhostVariable("filtercoeff_farras");          // wavelet for the first scale (a 2x10 matrix)
		QValue w2 = QValue.readhostVariable("filtercoeff_selcw").at(3, 1);  // wavelet for the other scales (a 2x12 matrix)
		
//		QFunction imwrite = new QFunction("imwrite(string,mat)");
//		System.out.println("imwrite mat exists? " + QHost.functionExists("imwrite"));
//		imwrite.apply(new QValue("e:\\imagecube.tif"), imageCube);
//		System.out.println("saved image cube");
				
		QValue result = waveletThresholding.apply(imageCube,
				                                  new QValue(WaveletThresholdingParams.sigma),
				                                  new QValue(WaveletThresholdingParams.J),
				                                  w1,
				                                  w2,
				                                  new QValue(WaveletThresholdingParams.thresholding),
				                                  new QValue(params.alpha));
		
		byte[] outputPixels = QUtils.newGrayscaleArrayFromCube(image.width, image.height, result);
		
		result.delete();
		imageCube.delete();
		
		return outputPixels;
	}
}