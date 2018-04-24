package com.scienceroot.search.preprint;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 *
 * @author svenseemann
 */
@Service
public class PreprintService {
    
    private final String API_URL = "https://share.osf.io/api/v2/search/creativeworks/_search";
    
    /**
     *
     * @param q
     * @return
     * @throws IOException
     */
    public List<Preprint> search(String q) throws IOException {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> entity = new HttpEntity<>(this.getBody(q) ,headers);
        
        RestTemplate template = new RestTemplate();
        ResponseEntity<String> response = template.exchange(this.API_URL, HttpMethod.POST, entity, String.class);
        
        ObjectMapper mapper = new ObjectMapper();
        JsonNode resultSet = mapper.readTree(response.getBody()).get("hits").get("hits");
        List<Preprint> resultPreprints = new LinkedList<>();
        
        if(resultSet.isArray()) {
            for (final JsonNode result : resultSet) {
                resultPreprints.add(new Preprint(result.get("_source")));
            }
        }
        
        return resultPreprints;
    }
    
    private String getBody(String query) {
        ObjectMapper mapper = new ObjectMapper();
          
        ObjectNode boolNode = mapper.createObjectNode();
        boolNode.putPOJO("must", this.getMustNode(query));
        boolNode.putPOJO("filter", this.getFiltersArray());
        
        ObjectNode queryNode = mapper.createObjectNode();
        queryNode.putPOJO("bool", boolNode);
        
        ObjectNode rootNode = mapper.createObjectNode();
        rootNode.putPOJO("query", queryNode);
        
        return rootNode.toString();
    }
    
    private JsonNode getMustNode(String q) {
        ObjectMapper mapper = new ObjectMapper();
        
        ObjectNode queryStringQueryNode = mapper.createObjectNode();
        queryStringQueryNode.put("query", q);
        
        ObjectNode queryStringNode = mapper.createObjectNode();
        queryStringNode.putPOJO("query_string", queryStringQueryNode);
        
        return queryStringNode;
    }
   
    private JsonNode getFiltersArray() {
        ObjectMapper mapper = new ObjectMapper();
        
        ArrayNode filtersNode = mapper.createArrayNode();
        ObjectNode filterTermNode = mapper.createObjectNode();
        ObjectNode filterTermTypeNode = mapper.createObjectNode();
        filterTermTypeNode.put("type", "preprint");
        filterTermNode.putPOJO("term", filterTermTypeNode);
        filtersNode.add(filterTermNode);
        
        return filtersNode;
    }
}
