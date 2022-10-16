package com.thesis.tattootopy.controller;

import com.thesis.tattootopy.model.Tattoo;
import com.thesis.tattootopy.model.User;
import com.thesis.tattootopy.modelDTO.GenerateTattooDTO;
import com.thesis.tattootopy.modelDTO.TattooDTO;
import com.thesis.tattootopy.modelDTO.UserDTO;
import com.thesis.tattootopy.service.interfaces.ITattooService;
import com.thesis.tattootopy.service.interfaces.IUserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.support.MissingServletRequestPartException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/tattOOtopy")
public class TattootopyController {

    @Autowired
    private IUserService userService;

    @Autowired
    private ITattooService tattooService;

    private static final Logger logger= LogManager.getLogger();

    @CrossOrigin
    //@PostMapping("/login")
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ResponseEntity<HttpStatus> login(@RequestBody UserDTO userDTO) {
        logger.info("UserController : login() is called - " + userDTO.getEmail());

        try {
            if (userDTO.getEmail() == null)
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST); // 400

            User fromRepository = userService.findUserByEmail(userDTO.getEmail());
            logger.info("UserController : user found - " + fromRepository);
            if (fromRepository != null) {
                if (userDTO.getHashedPassword().equals(fromRepository.getHashedPassword())) {
                    System.out.println("ok");
                    return new ResponseEntity<>(HttpStatus.OK); // 200
                } else {
                    System.out.println("pass not ok");
                    return new ResponseEntity<>(HttpStatus.UNAUTHORIZED); // 401
                }
            } else {
                System.out.println("user not found");
                return new ResponseEntity<>(HttpStatus.NOT_FOUND); // 404
                 }

        } catch (Exception ex){
            logger.error("POST MAPPING - Controller : login() - " + ex.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @CrossOrigin
    @PostMapping("/register")
    public ResponseEntity<HttpStatus> register(@RequestBody UserDTO userDTO) {
        User user = userDTO.toUser();
        logger.info("UserController : register() is called - " + user.getEmail());

        try {
            if (user.getEmail() != null && user.getHashedPassword() != null && user.getFirstName() != null && user.getLastName() != null) {
                User fromRepository = userService.findUserByEmail(user.getEmail());
                if (fromRepository == null) {
                    userService.save(user);
                    return new ResponseEntity<>(HttpStatus.OK); // 200
                } else
                    return new ResponseEntity<>(HttpStatus.UNAUTHORIZED); // 401
            }
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST); // 400

        } catch (Exception ex){
            logger.error("POST MAPPING - Controller : login() - " + ex.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @CrossOrigin
    @RequestMapping("/user-greeting")
    public String greeting(@RequestParam(value="name", defaultValue="World") String name) {
        return String.format("Hello, %s!", name);
    }

    @CrossOrigin
    @RequestMapping("/greeting")
    public void greeting() {
        System.out.println(String.format("Hello!"));
    }

    // -------------------------------------------------------------- GET LIST OF ENTITIES
    @CrossOrigin
    @PostMapping("all-tattoos-of-user")
    public ResponseEntity<List<TattooDTO>> getAllTattoosOfUser(@RequestBody UserDTO userDTO) {
        logger.info("POST MAPPING - UserController : getAllTattoosOfUser() is called - ");

        try {
            if (userDTO.getEmail() != null && !Objects.equals(userDTO.getEmail(), "")) {
                ArrayList<TattooDTO> all = new ArrayList<>();
                userService.findAllTattoosOfUser(userDTO.getEmail()).forEach(x -> {
                    TattooDTO tattooDTO = new TattooDTO();
                    tattooDTO.setId(x.getId());
                    tattooDTO.setDescription(x.getDescription());
                    tattooDTO.setPath(x.getPath());
                    tattooDTO.setPublic(x.isPublic());

                    all.add(tattooDTO);
                });
                logger.info(all.size());
                return new ResponseEntity<>(all, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }

        } catch (Exception ex){
            logger.error("POST MAPPING - Controller : getAllTattoosOfUser() - " + ex.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @CrossOrigin
    @PostMapping("search-by-keyword/{string}")
    public ResponseEntity<List<TattooDTO>> getTattoosWithDescriptionLike(@PathVariable String string) {
        logger.info("POST MAPPING - UserController : getTattoosWithDescriptionLike(   " + string + " ) is called - ");

        try {
            if (string != null && !Objects.equals(string, "")) {
                ArrayList<TattooDTO> all = new ArrayList<>();
                tattooService.findAllTattoosWithDescriptionLike(string).forEach(x -> {
                    TattooDTO tattooDTO = new TattooDTO();
                    tattooDTO.setId(x.getId());
                    tattooDTO.setDescription(x.getDescription());
                    tattooDTO.setPath(x.getPath());
                    tattooDTO.setPublic(x.isPublic());

                    all.add(tattooDTO);
                });
                logger.info(all.size());
                return new ResponseEntity<>(all, HttpStatus.OK);
            } else
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        } catch (Exception ex){
            logger.error("POST MAPPING - UserController : getTattoosWithDescriptionLike() - " + ex.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @CrossOrigin
    @GetMapping("demo")
    public ResponseEntity<List<TattooDTO>> getDemoGeneratedTattoos() {
        logger.info("GET MAPPING - UserController : getDemoGeneratedTattoos( ) is called - ");

        try {
            ArrayList<TattooDTO> all = new ArrayList<>();
            tattooService.findAllDemoTattoos().forEach(x -> {
                TattooDTO tattooDTO = new TattooDTO();
                tattooDTO.setId(x.getId());
                tattooDTO.setDescription(x.getDescription());
                tattooDTO.setPath(x.getPath());

                all.add(tattooDTO);
            });
            logger.info(all.size());
            return new ResponseEntity<>(all, HttpStatus.OK);

        } catch (Exception ex){
            logger.error("GET MAPPING - UserController : getDemoGeneratedTattoos() - " + ex.getMessage());
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // -------------------------------------------------------------- CRUD

    @CrossOrigin
    @PostMapping("update-tattoo")
    public ResponseEntity<HttpStatus> updateTattoo(@RequestBody TattooDTO tattooDTO) {
        logger.info("POST MAPPING - UserController : updateTattoo( ) is called - ");

        try {
            if (tattooDTO != null && tattooDTO.getId() != null) {
                if (tattooService.update(tattooDTO.toTattoo()))
                    return new ResponseEntity<>(HttpStatus.OK); // 200
                else
                    return new ResponseEntity<>(HttpStatus.NOT_FOUND); // 404
            }
            else{
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }

        } catch (Exception ex){
            logger.error("POST MAPPING - UserController : updateTattoo() - " + ex.getMessage());
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @CrossOrigin
    @DeleteMapping("delete-tattoo/{id}")
    public ResponseEntity<HttpStatus> deleteTattoo(@PathVariable Long id) {
        logger.info("DELETE MAPPING - UserController : deleteTattoo( ) is called - ");

        try {
            if (tattooService.deleteTattooById(id))
                return new ResponseEntity<>(HttpStatus.OK); // 200
            else
                return new ResponseEntity<>(HttpStatus.NOT_FOUND); // 404

        } catch (Exception ex){
            logger.error("DELETE MAPPING - UserController : deleteTattoo() - " + ex.getMessage());
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @CrossOrigin
    @PostMapping("generate-tattoo")
    public ResponseEntity<HttpStatus> generateTattoo(@RequestBody GenerateTattooDTO generateTattooDTO) {
        logger.info("GET MAPPING - UserController : generateTattoo( " + generateTattooDTO + " ) is called - ");

        try {
            if (generateTattooDTO.getUserEmailAddress() != null && !Objects.equals(generateTattooDTO.getUserEmailAddress(), "") && generateTattooDTO.getDescription() != null && !Objects.equals(generateTattooDTO.getDescription(), "")) {
                User user = userService.findUserByEmail(generateTattooDTO.getUserEmailAddress());
                if(user != null){
                    Tattoo tattoo = new Tattoo(generateTattooDTO.getDescription());
                    Tattoo returned = tattooService.save(tattoo);

                    if(returned != null){
                        user.addTattoo(returned);
                        userService.update(user);

                        tattooService.generateTattoo(returned, generateTattooDTO.getUserEmailAddress(), generateTattooDTO.getDescription(), "");
                        // client has to wait to result // will be received in email also
                        return new ResponseEntity<>(HttpStatus.OK); // 200
                    }
                    else
                        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR); // saved but not found in the database
                }
                else
                    return new ResponseEntity<>(HttpStatus.BAD_REQUEST); // user with sent email address could not be found in the database
            }
            else{
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST); // bad fields sent
            }

        } catch (Exception ex){
            logger.error("GET MAPPING - UserController : generateTattoo() - " + ex.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @CrossOrigin
    @PostMapping(value = "generate-tattoo-without-image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<HttpStatus> generateTattooWithoutImage(@RequestPart("description") String descriptiom , @RequestPart("userEmailAddress") String userEmailAddress) throws IOException {
        //logger.info("POST MAPPING - UserController : generateTattooDoi( " + d.getBodyPart("file").getOriginalFilename() + " ) is called - ");
        logger.info("POST MAPPING - UserController : generateTattooWithoutImage() is called - ");

        String to = "";

        try {
            if (userEmailAddress != null && !userEmailAddress.equals("") && descriptiom != null && !descriptiom.equals("")) {
                User user = userService.findUserByEmail(userEmailAddress);
                if(user != null){
                    Tattoo tattoo = new Tattoo(descriptiom);
                    Tattoo returned = tattooService.save(tattoo);

                    if(returned != null){
                        user.addTattoo(returned);
                        userService.update(user);

                        tattooService.generateTattoo(returned, userEmailAddress, descriptiom, "");
                        // client has to wait to result // will be received in email also
                        return new ResponseEntity<>(HttpStatus.OK); // 200
                    }
                    else
                        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR); // saved but not found in the database
                }
                else
                    return new ResponseEntity<>(HttpStatus.BAD_REQUEST); // user with sent email address could not be found in the database
            }
            else{
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST); // bad fields sent
            }

        } catch (Exception ex){
            logger.error("GET MAPPING - UserController : generateTattoo() - " + ex.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @CrossOrigin
    @PostMapping(value = "generate-tattoo-with-image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<HttpStatus> generateTattooWithImage(@RequestPart("file") MultipartFile file, @RequestPart("description") String descriptiom , @RequestPart("userEmailAddress") String userEmailAddress) throws IOException {
        //logger.info("POST MAPPING - UserController : generateTattooDoi( " + d.getBodyPart("file").getOriginalFilename() + " ) is called - ");
        logger.info("POST MAPPING - UserController : generateTattooWithImage( " + file.getOriginalFilename() + " ) is called - ");
        logger.info("POST MAPPING - UserController : generateTattooWithImage( " + descriptiom + " ) is called - ");
//        System.out.println(file.getResource());
//        System.out.println(file.getContentType());
//        System.out.println(file.getName());
//        String to = "../../../../../../opt/lampp/htdocs/tattootopyImages/inspiration/"+userEmailAddress+"_"+file.getOriginalFilename();
//        file.transferTo(new File(to));
        //return new ResponseEntity<>(HttpStatus.OK); // 200

        String to = "../../../../../../opt/lampp/htdocs/tattootopyImages/inspiration/"+userEmailAddress+"_"+file.getOriginalFilename();
        file.transferTo(new File(to));

        try {
            if (userEmailAddress != null && !userEmailAddress.equals("") && descriptiom != null && !descriptiom.equals("")) {
                User user = userService.findUserByEmail(userEmailAddress);
                if(user != null){
                    Tattoo tattoo = new Tattoo(descriptiom);
                    Tattoo returned = tattooService.save(tattoo);

                    if(returned != null){
                        user.addTattoo(returned);
                        userService.update(user);

                        tattooService.generateTattoo(returned, userEmailAddress, descriptiom, to);
                        // client has to wait to result // will be received in email also
                        return new ResponseEntity<>(HttpStatus.OK); // 200
                    }
                    else
                        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR); // saved but not found in the database
                }
                else
                    return new ResponseEntity<>(HttpStatus.BAD_REQUEST); // user with sent email address could not be found in the database
            }
            else{
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST); // bad fields sent
            }

        } catch (Exception ex){
            logger.error("GET MAPPING - UserController : generateTattoo() - " + ex.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }



}
