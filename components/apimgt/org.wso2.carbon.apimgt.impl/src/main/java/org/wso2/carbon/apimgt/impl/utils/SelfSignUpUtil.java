/*
 * Copyright (c) 2005-2011, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 * 
 * WSO2 Inc. licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.wso2.carbon.apimgt.impl.utils;

import org.wso2.carbon.apimgt.api.APIManagementException;
import org.wso2.carbon.apimgt.impl.dto.UserRegistrationConfigDTO;
import org.wso2.carbon.apimgt.impl.internal.ServiceReferenceHolder;
import org.wso2.carbon.user.core.UserCoreConstants;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * This class contains the utility methods used for self signup
 */
public final class SelfSignUpUtil {


	/**
	 * Retrieve self signup configuration from the Advanced Configuration and return the configuration
	 *
	 * @param tenantDomain Domain name of the tenant
	 * @return UserRegistrationConfigDTO self signup configuration for the tenant
	 * @throws APIManagementException
	 */
	public static UserRegistrationConfigDTO getSignupConfiguration(String tenantDomain) throws APIManagementException {

		Object selfSighupConfigObject = ServiceReferenceHolder.getInstance().getApimConfigService()
				.getSelfSighupConfig(tenantDomain);
		if (selfSighupConfigObject instanceof UserRegistrationConfigDTO) {
			UserRegistrationConfigDTO selfSighupConfig = (UserRegistrationConfigDTO) selfSighupConfigObject;
			return selfSighupConfig;
		}
		return new UserRegistrationConfigDTO();
	}

	/**
	 * modify username with user storage information.
	 *
	 * @param username     - The username
	 * @param signupConfig - The sign-up configuration
	 * @return - The modified username
	 */
	public static String getDomainSpecificUserName(String username, UserRegistrationConfigDTO signupConfig) {
		String modifiedUsername = null;
		// set tenant specific sign up user storage
		if (signupConfig != null && signupConfig.getSignUpDomain() != null && !signupConfig.getSignUpDomain().equals("")) {
			int index = username.indexOf(UserCoreConstants.DOMAIN_SEPARATOR);
			/*
			 * if there is a different domain provided by the user other than one
			 * given in the configuration, add the correct signup domain. Here signup
			 * domain refers to the user storage
			 */
			if (index > 0) {
				modifiedUsername = signupConfig.getSignUpDomain().toUpperCase() + UserCoreConstants.DOMAIN_SEPARATOR +
								username.substring(index + 1);
			} else {
				modifiedUsername = signupConfig.getSignUpDomain().toUpperCase() + UserCoreConstants.DOMAIN_SEPARATOR +
						username;
			}
		}
		return modifiedUsername;
	}
}
