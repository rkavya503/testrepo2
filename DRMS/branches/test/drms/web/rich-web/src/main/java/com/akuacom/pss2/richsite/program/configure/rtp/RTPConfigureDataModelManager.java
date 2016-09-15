package com.akuacom.pss2.richsite.program.configure.rtp;

import java.io.File;
import java.io.IOException;

public interface RTPConfigureDataModelManager {
	/**
	 * Function for save rtp csv file from presentation layer data into database
	 * @param model
	 * @param file
	 * @return
	 * @throws IOException
	 */
	public String saveRTPFileIntoDB(RTPConfigureDataModel model,File file) throws IOException;
	/**
	 * Function for get RTPConfig object from database
	 * @param model
	 * @throws Exception
	 */
	public void getRTPConfigs(RTPConfigureDataModel model) throws Exception;
}
