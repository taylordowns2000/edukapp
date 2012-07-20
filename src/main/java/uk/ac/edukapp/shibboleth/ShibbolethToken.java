/*
 * Copyright 2012 SURFnet bv, The Netherlands
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */

package uk.ac.edukapp.shibboleth;

import java.util.Map;

import org.apache.shiro.authc.AuthenticationToken;

/**
 * Class that holds principal and assertions gotten from Shibboleth.
 *
 * @author Geert van der Ploeg
 */
public class ShibbolethToken implements AuthenticationToken {

  String username;

  private Map<String, String> assertions;


  public ShibbolethToken(String username, Map<String, String> assertions) {
    this.username = username;
    this.assertions = assertions;
  }

  public Object getPrincipal() {
    return username;
  }

  public Object getCredentials() {
    return null;
  }

  public String getUsername() {
    return username;
  }
}
