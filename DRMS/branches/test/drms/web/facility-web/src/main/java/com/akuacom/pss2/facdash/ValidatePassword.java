// $Revision$ $Date$
package com.akuacom.pss2.facdash;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.akuacom.pss2.util.PSS2Util;
import com.akuacom.utils.MD5Tool;

public class ValidatePassword
{
	/** The Constant log. */
	private static final Logger log = Logger.getLogger(ValidatePassword.class);
    public static final String NAV_FORM_CHECK_PW = "navForm:checkPW";

    /**
     * test password against db.
     *
     * @param dbPassword hashed password from db
     * @param checkPassword password to be tested
     * @return true if matches, false otherwise
     */
    public static boolean validate(String dbPassword, String checkPassword) {
        boolean valid = false;
        FDUtils.clearMsg(NAV_FORM_CHECK_PW);
        try {
            if (!dbPassword.equals(MD5Tool.getHashString(checkPassword))) {
                FDUtils.addMsgError(NAV_FORM_CHECK_PW,"Password FAILED");
            } else {
                FDUtils.addMsgInfo(NAV_FORM_CHECK_PW, "Password OK");
                valid = true;
            }
        } catch (NoSuchAlgorithmException e) {
            log.error("Error setting password", e);
            FDUtils.addMsgError("Internal error");
        }
        return valid;
    }


	public static boolean validate(String dbPassword, String currentPassword, 
		String newPassword, String confirmNewPassword, boolean clir)
	{
		boolean errors = validate(newPassword, confirmNewPassword, clir);
		try
		{
			if (!dbPassword.equals(MD5Tool.getHashString(currentPassword)))
			{
				FDUtils.addMsgError("Current Password must match existing password");
				errors = false;
			}
			else if (dbPassword.equals(MD5Tool.getHashString(newPassword)))
			{
				FDUtils.addMsgError("New Password can't be the same as Current Password");
				errors = false;
			}
		}
		catch (NoSuchAlgorithmException e)
		{
			log.error("Error setting password", e);
			FDUtils.addMsgError("Internal error");
			errors = false;
		}
		return errors;
	}

	public static boolean validate(String password, String confirmPassword,
		boolean clir)
	{
		List<String> messages = new ArrayList<String>();
		boolean rv = PSS2Util.validatePassword(password, confirmPassword,
			clir, messages);
		for(String message: messages)
		{
			FDUtils.addMsgError(message);
		}
		return rv;
	}

}
