package indi.pet.producer.service;

import indi.pet.producer.domain.Resource;
import indi.pet.producer.repository.ResourceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.Collection;

/**
 * @author <a href="maimengzzz@gmail.com">韩超</a>
 * @since 2018.11.05
 */
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

    public Resource findById(String id){
        return getResourceRepository().findOne(id);
    }

    public void deleteByIds(Collection<String> ids){
        for(String id : ids){
            deleteById(id);
        }
    }

    public void deleteById(String id){
        Resource resource=findById(id);
        File file=new File(resource.getPath());
        if (file.exists())
            file.delete();
        getResourceRepository().delete(id);
    }

}
