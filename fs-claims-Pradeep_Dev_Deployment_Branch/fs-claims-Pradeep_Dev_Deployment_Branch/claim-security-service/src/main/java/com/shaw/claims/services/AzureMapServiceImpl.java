package com.shaw.claims.services;

import com.azure.core.credential.AzureKeyCredential;
import com.azure.maps.search.MapsSearchClient;
import com.azure.maps.search.MapsSearchClientBuilder;
import com.azure.maps.search.models.SearchAddressOptions;
import com.azure.maps.search.models.SearchAddressResult;
import com.azure.maps.search.models.SearchAddressResultItem;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shaw.claims.dto.GeoLocationDTO;
import com.shaw.claims.dto.RouteSummaryDTO;
import com.shaw.claims.model.Locations;
import com.shaw.claims.repo.LocationRepository;
import com.shaw.claims.util.CoordinateParser;
import com.shaw.claims.util.JsonMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.*;

@Service
public class AzureMapServiceImpl implements AzureMapService {
    @Value("${azure.maps.subscription-key}")
    private String subscriptionKey;
    @Autowired
    LocationRepository locationRepository;
    @Value("${azure.maps.base-url}")
    private String baseUrl;
    @Autowired
    private RestTemplate restTemplate;
    private final double[][] destinations = {{28.412030000,-81.363200000},{29.601440000,-98.363800000},{29.876090000,-95.557720000},{30.577600000,-88.164900000},{32.199490000,-81.176390000},{32.933630000,-97.004460000},{33.460330000,-112.217550000},{33.796760000,-118.030740000},{34.438790000,-84.910450000},{34.677000000,-85.001720000},{34.681850000,-84.997390000},{34.906200000, -85.114910000},{35.015780000, -89.855370000},{35.080220000, -106.723270000},{35.115550000, -80.918910000},{35.471340000,-97.603830000},{36.060490000,-115.222110000},{37.605740000,-122.084100000},{38.819660000,-90.836150000},{38.917320000,-94.783400000},{39.174860000,-76.714450000},{39.344590000,-84.491510000},{39.772550000,-104.868420000},{40.324350000,-74.505900000},{40.568330000,-80.221090000},{40.741990000,-111.975660000},{41.724490000,-85.930440000},{41.942400000,-71.134030000},{42.094170000,-88.344920000},{42.254710000,-83.523370000},{44.852310000,-93.134320000},{45.596960000,-122.760630000},{47.400230000,-122.253750000}};
    @Override
    public String getLocation(List<String> addrLst) {
        String coordinates = "";
        MapsSearchClientBuilder builder = new MapsSearchClientBuilder();
        AzureKeyCredential keyCredential = new  AzureKeyCredential(subscriptionKey);
        builder.credential(keyCredential);

        MapsSearchClient client = builder.buildClient();

        addrLst.forEach(address -> {
            SearchAddressResult results = client.searchAddress(new SearchAddressOptions(address));
            if (results.getResults().size() > 1) {
                System.out.print("Address :: " + address + " -- ");
                results.getResults().forEach(d-> {
                    System.out.println(d.getAddress().getFreeformAddress());
                });
            }/*else{
                results.getResults().forEach(d-> {
                    System.out.println(d.getAddress().getFreeformAddress()+"----"+d.getPosition().getLatitude() +","+ d.getPosition().getLongitude());
                });
            }*/
        });

        /*SearchAddressResult results = client.searchAddress(new SearchAddressOptions(address));
        System.out.println("Locations Size :: " + results.getResults().size());
        if (results.getResults().size() > 0) {
            results.getResults().forEach(d-> {
                System.out.println(d.getAddress().getFreeformAddress()+"----"+d.getPosition().getLatitude() +","+ d.getPosition().getLongitude());
            });
        }*/


        /*FuzzySearchOptions options = new FuzzySearchOptions()
        client.fuzzySearch()*/

        /* SearchAddressResult results = client.searchAddress(new SearchAddressOptions("1 " +
                "Main Street").setCoordinates(new GeoPosition(-74.011454,
        40.706270)).setRadiusInMeters(40000).setTop(5));*/
           /* SearchAddressResultItem item = results.getResults().get(0);
            coordinates = item.getPosition().getLatitude() +","+ item.getPosition().getLongitude();
            System.out.println(coordinates);*/

        return "";
    }

    @Override
    public String getCoordinates(String address) {
        String coordinates = "";
        MapsSearchClientBuilder builder = new MapsSearchClientBuilder();
        AzureKeyCredential keyCredential = new  AzureKeyCredential(subscriptionKey);
        builder.credential(keyCredential);
        MapsSearchClient client = builder.buildClient();
        SearchAddressResult results = client.searchAddress(new SearchAddressOptions(address));
        System.out.println("Locations Size :: " + results.getResults().size());
        if (results.getResults().size() > 0) {
            results.getResults().forEach(d-> {
                System.out.println(d.getAddress().getFreeformAddress()+"----"+d.getPosition().getLatitude() +","+ d.getPosition().getLongitude());
            });
        }
        return "";
    }

    public String getCoordinates1234(String address) {
        String coordinates = "";
        MapsSearchClientBuilder builder = new MapsSearchClientBuilder();
//        String subKey = subscriptionKey.substring(2, subscriptionKey.length() - 2);
        AzureKeyCredential keyCredential = new  AzureKeyCredential(subscriptionKey);
        builder.credential(keyCredential);
        MapsSearchClient client = builder.buildClient();
  /*      FuzzySearchOptions options = new FuzzySearchOptions()
        client.fuzzySearch()*/
        SearchAddressResult results = client.searchAddress(new SearchAddressOptions(address));
       /* SearchAddressResult results = client.searchAddress(new SearchAddressOptions("1 " +
                "Main Street").setCoordinates(new GeoPosition(-74.011454,
        40.706270)).setRadiusInMeters(40000).setTop(5));*/
        System.out.println("Locations Size :: " + results.getResults().size());
        // Print results:
        if (!results.getResults().isEmpty()) {
            SearchAddressResultItem item = results.getResults().get(0);
            coordinates = "POINT("+item.getPosition().getLatitude()+" ,"+ item.getPosition().getLongitude()+")";
//            coordinates = item.getPosition().getLatitude() + "," +item.getPosition().getLongitude();
            System.out.println(coordinates);
        }
        return coordinates;
    }
    @Override
    public void updateLocation(Integer locationId) {
        Optional<Locations> optLocations = locationRepository.findById(locationId);
        if(optLocations.isPresent()){
            Locations loc = optLocations.get();
//            String coordinates =  getCoordinates(loc.getFullAddress());
            String coordinates ="39.17486,-76.71445";
//            coordinates = "POINT(39.17486 -76.71445)";
            String [] coord = coordinates.split(",");
            loc.setGeoLocation("POINT(39.17486 -76.71445)");
            loc.setLatitude(Double.parseDouble(coord[0]));
            loc.setLongitude(Double.parseDouble(coord[1]));
//            locationRepository.save(loc);
        }
    }

    @Override
    public GeoLocationDTO findClosestRDC(GeoLocationDTO origin) {
        String response = null;
        GeoLocationDTO nearestLatLong = null;
        ObjectMapper objectMapper = new ObjectMapper();
        List<GeoLocationDTO> origins = new ArrayList<>();
        List<GeoLocationDTO> destinations = null;
        try {
            origins.add(origin);
            destinations = locationRepository.getAllLocations();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            String url = UriComponentsBuilder.fromHttpUrl("https://atlas.microsoft.com/route/matrix/sync/json")
                    .queryParam("subscription-key", subscriptionKey)
                    .queryParam("api-version", "1.0")
                    .queryParam("waitForResults", true)
                    /*.queryParam("travelMode", "car")
                    .queryParam("routeType", "shortest")*/
                    .build()
                    .toUriString();
            System.out.println("URL :: "+url);
            String requestBody = buildRouteMatrixRequestBody(origins, destinations);
            System.out.println("Req Body :: "+requestBody);
            HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);
            response = restTemplate.postForObject(url, entity, String.class);
            List<RouteSummaryDTO> routeSummaryDTOList = JsonMapper.mapJsonToDTO(response);
            Map<Integer, GeoLocationDTO> map = CoordinateParser.parseCoordinatesToMap(destinations);
            if(map.containsKey(routeSummaryDTOList.get(1).getIndex())){
                nearestLatLong = map.get(routeSummaryDTOList.get(1).getIndex());
            }
        }catch (Exception ex){
            System.out.println(ex.getMessage());
        }
        return nearestLatLong;
    }

    private String buildRouteMatrixRequestBody(List<GeoLocationDTO> origins, List<GeoLocationDTO> destinations) throws Exception {
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("origins", convertToLatLonList(origins));
        requestBody.put("destinations", convertToLatLonList(destinations));
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(requestBody);
    }
    private Map<String, Object> convertToLatLonList(List<GeoLocationDTO> coordinates) {
        List<Object> list = new ArrayList<>();
        Map<String, Object> latLon = new HashMap<>();
        latLon.put("type", "MultiPoint");
        for (GeoLocationDTO loc : coordinates) {
            list.add(List.of(loc.getLongitude(), loc.getLatitude()));
        }
        latLon.put("coordinates",list);
        return latLon;
    }

}
