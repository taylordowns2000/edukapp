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

package uk.ac.edukapp.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

@Entity
@Table(name = "syndicatedwidgetprofiles")
@NamedQueries({
    @NamedQuery(name = "SyndicatedWidgetprofile.findByUri",
        query = "SELECT w FROM SyndicatedWidgetprofile w WHERE w.uri = :uri")
})
public class SyndicatedWidgetprofile implements Serializable {

  private static final long serialVersionUID = 1L;

  public SyndicatedWidgetprofile() {
  }

  public SyndicatedWidgetprofile(String sourceUri, String name, String license, String author, String widgetId,
                                 String description, String uri, Date updated, Date created) {
    this.sourceUri = sourceUri;
    this.name = name;
    this.license = license;
    this.author = author;
    this.widgetId = widgetId;
    this.description = description;
    this.uri = uri;
    this.updated = updated;
    this.created = created;
  }

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private int id;

  @Column
  private String sourceUri;

  @Column
  private String name;

  @Column(columnDefinition = "text(65535) default null")
  private String license;

  @Column
  private String author;

  @Column
  private String widgetId;

  @Column
  private String description;

  @Column
  private String uri;

  @Column
  private Date updated;

  @Column
  private Date created;

  @Override
  public String toString() {
    return new ReflectionToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE).toString();
  }

  public int getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public String getWidgetId() {
    return widgetId;
  }

  public String getDescription() {
    return description;
  }

  public String getUri() {
    return uri;
  }

  public void setSourceUri(String sourceUri) {
    this.sourceUri = sourceUri;
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setLicense(String license) {
    this.license = license;
  }

  public void setAuthor(String author) {
    this.author = author;
  }

  public void setWidgetId(String widgetId) {
    this.widgetId = widgetId;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public void setUri(String uri) {
    this.uri = uri;
  }

  public void setUpdated(Date updated) {
    this.updated = updated;
  }

  public void setCreated(Date created) {
    this.created = created;
  }

  public Date getUpdated() {
    return updated;
  }

  public String getLicense() {
    return license;
  }

  public String getAuthor() {
    return author;
  }
}
