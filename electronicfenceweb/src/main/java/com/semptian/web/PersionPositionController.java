package com.semptian.web;

import com.semptian.entity.PositionView;
import com.semptian.service.PersionPositionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/position",produces = "application/json")
public class PersionPositionController {

    @Autowired
    private PersionPositionService persionPositionService;

    @RequestMapping(value = "/search.json",method = RequestMethod.POST)
    public Object search(@RequestBody PositionView position){
        return persionPositionService.search(position);
    }

    @RequestMapping(value = "/searchname.json",method = RequestMethod.GET)
    public Object serchName(String name){
        return persionPositionService.findName(name);
    }


}
