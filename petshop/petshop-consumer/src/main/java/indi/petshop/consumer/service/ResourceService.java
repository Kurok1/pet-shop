package indi.pet.consumer.service;

import indi.pet.consumer.domain.Resource;
import indi.pet.consumer.repository.ResourceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;

/**
 * @author <a href="maimengzzz@gmail.com">韩超</a>
 * @since 2019.02.24
 */
@Transactional
@Service("resourceService")
public class ResourceService {

    private ResourceRepository resourceRepository;

    public ResourceRepository getResourceRepository() {
        return resourceRepository;
    }

    @Autowired
    public void setResourceRepository(@Qualifier("resourceRepository") ResourceRepository resourceRepository) {
        this.resourceRepository = resourceRepository;
    }

    public Resource save(Resource resource){
        return getResourceRepository().save(resource);
    }

    public void delete(String id){
        //删除文件
        Resource resource=this.findOne(id);
        File file=new File(resource.getPath());
        if(file.exists()) {
            file.delete();
        }
        getResourceRepository().delete(id);
    }

    public void delete(Iterable<String> ids){
        getResourceRepository().deleteResourcesByIdIn(ids);
    }

    public Resource findOne(String id){
        return getResourceRepository().findOne(id);
    }

    public Iterable<Resource> find(Iterable<String> ids){
        return getResourceRepository().findAll(ids);
    }
}
