package be.vib.imagej;

import be.vib.bits.QFunction;
import be.vib.bits.QUtils;
import be.vib.bits.QValue;

class NonLocalMeansSCDDenoiser extends Denoiser
{
	private final NonLocalMeansSCDParams params;
	
	public NonLocalMeansSCDDenoiser(NonLocalMeansSCDParams params)
	{
		super();
		this.params = params;
	}
	
	@Override
	public byte[] call()
	{		
		QFunction nlmeansSCD = loadDenoiseFunction("E:\\git\\bits\\bioimaging\\EMDenoising\\src\\main\\resources\\quasar\\nlmeans_scd.q",
                                                   "deconv_nlmeans_sc(mat,mat,scalar,int,int,int,scalar,scalar,scalar,mat)");
				
		QValue imageCube = QUtils.newCubeFromGrayscaleArray(image.width, image.height, image.pixels);

		QFunction fgaussian = new QFunction("fgaussian(int,scalar)");
		QValue blurKernel = fgaussian.apply(new QValue(NonLocalMeansSCDParams.blurKernelSize), new QValue(NonLocalMeansSCDParams.blurKernelSigma)); 

		QFunction imwrite = new QFunction("imwrite(string,cube)");
		imwrite.apply(new QValue("e:\\nlms_scd_input_java.tif"), imageCube);

		// FIXME: QFunction.apply() does not support more than 8 arguments, and we're providing 10!
		//        (Is there a way to pass QFunction.apply a QValues[] instead? Does the Quasar C++ support this?)
	
		/*
				[ 510, 594, 4]
				[ 510, 594]
				Image deconvolution using non-local prior
				deconv_nlmeans_sc lambda=0.300000 num_iter=25 search_wnd=5 half_block_size=4 h=13.500000 sigma_0=20.000000 alpha=0.050000 
				
				H=blurkernel=
				[[ 8.34431001578478E-23, 5.55014797321383E-20, 1.35807646206395E-17, 1.22250147694845E-15, 4.04836889922669E-14, 4.93192292561062E-13, 2.21033456294928E-12, 3.64422554632871E-12, 2.21033456294928E-12, 4.93192292561062E-13, 4.04836889922669E-14, 1.22250147694845E-15, 1.35807646206395E-17, 5.55014797321383E-20, 8.34431001578478E-23 ], 
				 [ 5.55014797321383E-20, 3.69163428193026E-17, 9.03313204401665E-15, 8.13136613164039E-13, 2.69273856684515E-11, 3.28042731867484E-10, 1.47018552887346E-09, 2.42392594884677E-09, 1.47018552887346E-09, 3.28042731867484E-10, 2.69273856684515E-11, 8.13136613164039E-13, 9.03313204401665E-15, 3.69163428193026E-17, 5.55014797321383E-20 ], 
				 [ 1.35807646206395E-17, 9.03313204401665E-15, 2.21033456294928E-12,  1.9896796710217E-10, 6.58891430305175E-09, 8.02694088974931E-08, 3.59742529099094E-07, 5.93115146330092E-07, 3.59742529099094E-07, 8.02694088974931E-08, 6.58891430305175E-09,  1.9896796710217E-10, 2.21033456294928E-12, 9.03313204401665E-15, 1.35807646206395E-17 ], 
				 [ 1.22250147694845E-15, 8.13136613164039E-13,  1.9896796710217E-10, 1.79105263953261E-08, 5.93115146330092E-07, 7.22562208466115E-06, 3.23829917761032E-05, 5.33905258635059E-05, 3.23829917761032E-05, 7.22562208466115E-06, 5.93115146330092E-07, 1.79105263953261E-08,  1.9896796710217E-10, 8.13136613164039E-13, 1.22250147694845E-15 ], 
				 [ 4.04836889922669E-14, 2.69273856684515E-11, 6.58891430305175E-09, 5.93115146330092E-07, 1.96412765944842E-05, 0.000239279732340947,  0.00107237743213773,  0.00176805141381919,  0.00107237743213773, 0.000239279732340947, 1.96412765944842E-05, 5.93115146330092E-07, 6.58891430305175E-09, 2.69273856684515E-11, 4.04836889922669E-14 ], 
				 [ 4.93192292561062E-13, 3.28042731867484E-10, 8.02694088974931E-08, 7.22562208466115E-06, 0.000239279732340947,  0.00291502405889332,   0.0130642307922244,   0.0215392746031284,   0.0130642307922244,  0.00291502405889332, 0.000239279732340947, 7.22562208466115E-06, 8.02694088974931E-08, 3.28042731867484E-10, 4.93192292561062E-13 ], 
				 [ 2.21033456294928E-12, 1.47018552887346E-09, 3.59742529099094E-07, 3.23829917761032E-05,  0.00107237743213773,   0.0130642307922244,   0.0585498213768005,   0.0965323373675346,   0.0585498213768005,   0.0130642307922244,  0.00107237743213773, 3.23829917761032E-05, 3.59742529099094E-07, 1.47018552887346E-09, 2.21033456294928E-12 ], 
				 [ 3.64422554632871E-12, 2.42392594884677E-09, 5.93115146330092E-07, 5.33905258635059E-05,  0.00176805141381919,   0.0215392746031284,   0.0965323373675346,    0.159154921770096,   0.0965323373675346,   0.0215392746031284,  0.00176805141381919, 5.33905258635059E-05, 5.93115146330092E-07, 2.42392594884677E-09, 3.64422554632871E-12 ], 
				 [ 2.21033456294928E-12, 1.47018552887346E-09, 3.59742529099094E-07, 3.23829917761032E-05,  0.00107237743213773,   0.0130642307922244,   0.0585498213768005,   0.0965323373675346,   0.0585498213768005,   0.0130642307922244,  0.00107237743213773, 3.23829917761032E-05, 3.59742529099094E-07, 1.47018552887346E-09, 2.21033456294928E-12 ], 
				 [ 4.93192292561062E-13, 3.28042731867484E-10, 8.02694088974931E-08, 7.22562208466115E-06, 0.000239279732340947,  0.00291502405889332,   0.0130642307922244,   0.0215392746031284,   0.0130642307922244,  0.00291502405889332, 0.000239279732340947, 7.22562208466115E-06, 8.02694088974931E-08, 3.28042731867484E-10, 4.93192292561062E-13 ], 
				 [ 4.04836889922669E-14, 2.69273856684515E-11, 6.58891430305175E-09, 5.93115146330092E-07, 1.96412765944842E-05, 0.000239279732340947,  0.00107237743213773,  0.00176805141381919,  0.00107237743213773, 0.000239279732340947, 1.96412765944842E-05, 5.93115146330092E-07, 6.58891430305175E-09, 2.69273856684515E-11, 4.04836889922669E-14 ], 
				 [ 1.22250147694845E-15, 8.13136613164039E-13,  1.9896796710217E-10, 1.79105263953261E-08, 5.93115146330092E-07, 7.22562208466115E-06, 3.23829917761032E-05, 5.33905258635059E-05, 3.23829917761032E-05, 7.22562208466115E-06, 5.93115146330092E-07, 1.79105263953261E-08,  1.9896796710217E-10, 8.13136613164039E-13, 1.22250147694845E-15 ], 
				 [ 1.35807646206395E-17, 9.03313204401665E-15, 2.21033456294928E-12,  1.9896796710217E-10, 6.58891430305175E-09, 8.02694088974931E-08, 3.59742529099094E-07, 5.93115146330092E-07, 3.59742529099094E-07, 8.02694088974931E-08, 6.58891430305175E-09,  1.9896796710217E-10, 2.21033456294928E-12, 9.03313204401665E-15, 1.35807646206395E-17 ], 
				 [ 5.55014797321383E-20, 3.69163428193026E-17, 9.03313204401665E-15, 8.13136613164039E-13, 2.69273856684515E-11, 3.28042731867484E-10, 1.47018552887346E-09, 2.42392594884677E-09, 1.47018552887346E-09, 3.28042731867484E-10, 2.69273856684515E-11, 8.13136613164039E-13, 9.03313204401665E-15, 3.69163428193026E-17, 5.55014797321383E-20 ], 
				 [ 8.34431001578478E-23, 5.55014797321383E-20, 1.35807646206395E-17, 1.22250147694845E-15, 4.04836889922669E-14, 4.93192292561062E-13, 2.21033456294928E-12, 3.64422554632871E-12, 2.21033456294928E-12, 4.93192292561062E-13, 4.04836889922669E-14, 1.22250147694845E-15, 1.35807646206395E-17, 5.55014797321383E-20, 8.34431001578478E-23 ]]
				
				corr_filter_inv=[ 0.00354881025850773, 0.00645745964720845, 0.00715041672810912, 0.0103952502831817, 0.0187588091939688, 0.0213609132915735, 0.0452975630760193, 0.0392604991793633, 0.123410135507584, 0.0220631398260593, 0.443138360977173, -0.479376375675201, 1.95572137832642, -0.479376375675201, 0.443138360977173, 0.0220631398260593, 0.123410135507584, 0.0392604991793633, 0.0452975630760193, 0.0213609132915735, 0.0187588091939688, 0.0103952502831817, 0.00715041672810912, 0.00645745964720845, 0.00354881025850773]

		worked when called from Redshift.
		*/

		
		QValue result = nlmeansSCD.apply(imageCube,
				                         blurKernel,
				                         new QValue(params.lambda),
				                         new QValue(params.numIterations),
				                         new QValue(NonLocalMeansSCDParams.halfSearchSize),
				                         new QValue(NonLocalMeansSCDParams.halfBlockSize),
				                         new QValue(params.h),
				                         new QValue(params.sigma0),
				                         new QValue(NonLocalMeansSCDParams.alpha),
				                         new QValue(NonLocalMeansSCDParams.emCorrFilterInv));
		
		imwrite.apply(new QValue("e:\\nlms_scd_result_java.tif"), result);

		byte[] outputPixels = QUtils.newGrayscaleArrayFromCube(image.width, image.height, result);
		
		result.delete();
		imageCube.delete();		
		
		return outputPixels;
	}
}