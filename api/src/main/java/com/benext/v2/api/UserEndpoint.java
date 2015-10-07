package com.benext.v2.api;

import com.benext.v2.dto.UserRequest;
import com.benext.v2.dto.UserResponse;
import com.benext.v2.model.User;
import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;
import com.google.api.server.spi.response.ConflictException;
import com.google.api.server.spi.response.NotFoundException;
import com.googlecode.objectify.Key;

import java.util.logging.Logger;

import javax.inject.Named;

import static com.benext.services.OfyService.ofy;

/**
 * An endpoint class we are exposing
 */
@Api(
        name = "userApi",
        version = "v2",
        resource = "user",
        namespace = @ApiNamespace(
                ownerDomain = "api.benext.com",
                ownerName = "api.benext.com",
                packagePath = ""
        )
)
public class UserEndpoint {

    private static final Logger logger = Logger.getLogger(UserEndpoint.class.getName());

    /**
     * This method gets the <code>User</code> object associated with the specified <code>id</code>.
     *
     * @param id The id of the object to be returned.
     * @return The <code>User</code> associated with <code>id</code>.
     */
    @ApiMethod(name = "getUser")
    public UserResponse getUser(@Named("id") long id) throws NotFoundException{
        return new UserResponse(ofy().load().type(User.class).id(id).safe());
    }

    /**
     * This inserts a new <code>User</code> object.
     *
     * @param userRequest The object to be added.
     * @return The object to be added.
     */
    @ApiMethod(name = "insertUser")
    public UserResponse insertUser(UserRequest userRequest) throws ConflictException {
       Key<User> key = ofy().save().entity(new User(userRequest)).now();
       return new UserResponse(ofy().load().type(User.class).id(key.getId()).now());
    }

    /**
     * Updates an existing <code>User</code> object.
     *
     * @param id  the ID of the entity to be updated
     * @param userRequest the desired state of the entity
     * @return the updated version of the entity
     * @throws NotFoundException if the {@code id} does not correspond to an existing
     *                           {@code Consensus}
     */
    @ApiMethod(name = "updateUser",
        path="user/{id}")
    public void updateUser(@Named("id") long id, UserRequest userRequest) {
        User user = ofy().load().type(User.class).id(id).safe();
        ofy().save().entity(user.update(userRequest)).now();
    }



    //Private method to retrieve a <code>Quote</code> record
    private User findRecord(String id) {
        return ofy().load().type(User.class).id(id).now();
//or return ofy().load().type(Quote.class).filter("id",id).first.now();
    }

}