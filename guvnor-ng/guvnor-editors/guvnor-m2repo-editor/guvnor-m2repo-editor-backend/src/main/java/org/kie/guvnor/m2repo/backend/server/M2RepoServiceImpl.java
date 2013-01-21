/*
 * Copyright 2012 JBoss Inc
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.kie.guvnor.m2repo.backend.server;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.jboss.errai.bus.server.annotations.Service;
import org.kie.guvnor.commons.data.tables.PageRequest;
import org.kie.guvnor.commons.data.tables.PageResponse;
import org.kie.guvnor.m2repo.model.JarListPageRow;
import org.kie.guvnor.m2repo.service.M2RepoService;
import org.kie.guvnor.project.model.GAV;


/**
 *
 */
@Service
@ApplicationScoped
public class M2RepoServiceImpl
        implements M2RepoService {

    @Inject
    private GuvnorM2Repository repository;


    @Override
    public void deployJar(InputStream is, GAV gav) {
        repository.deployArtifact(is, gav);
    }
    
    @Override
    public InputStream loadJar(String path) {
        return repository.loadFile(path);
    }
    
    @Override
    public String getJarName(String path) {
        return repository.getFileName(path);
    }
    
    @Override
    public void deleteJar(String[] path) {
        repository.deleteFile(path);
    }
    
    @Override
    public String loadPOMStringFromJar(String path) {
        return repository.loadPOMFromJar(path);
    }
    
    @Override
    public PageResponse<JarListPageRow> listJars( PageRequest pageRequest, String filters ) {
        Collection<File> files = repository.listFiles(filters);
        
        PageResponse<JarListPageRow> response = new PageResponse<JarListPageRow>();
        List<JarListPageRow> tradeRatePageRowList = new ArrayList<JarListPageRow>();

        int i = 0;
        for(File file : files) {
            if(i>=pageRequest.getStartRowIndex() + pageRequest.getPageSize()) {
                break;
            }
            if(i>=pageRequest.getStartRowIndex()) {
                JarListPageRow jarListPageRow = new JarListPageRow();
                jarListPageRow.setName(file.getName());
                jarListPageRow.setPath(file.getPath());
                jarListPageRow.setLastModified(new Date(file.lastModified()));
                tradeRatePageRowList.add(jarListPageRow);
            }
            i++;            
        }
        
        response.setPageRowList(tradeRatePageRowList);
        response.setStartRowIndex(pageRequest.getStartRowIndex());
        response.setTotalRowSize(files.size());
        response.setTotalRowSizeExact(true);
        //response.setLastPage(true);
        
        return response;
    }
    
    @Override
    public String getRepositoryURL() {
        return repository.getRepositoryURL();
    }
    

}