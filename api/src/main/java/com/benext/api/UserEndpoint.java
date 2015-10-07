package com.benext.api;

import com.benext.model.Consensus;
import com.benext.model.User;
import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;
import com.google.api.server.spi.response.ConflictException;
import com.google.api.server.spi.response.NotFoundException;

import java.util.logging.Logger;

import javax.inject.Named;

import static com.benext.services.OfyService.ofy;

/**
 * An endpoint class we are exposing
 */
@Api(
        name = "userApi",
        version = "v1",
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
    public User getUser(@Named("id") String id) throws NotFoundException{
        User user = findRecord(id);
        if (user == null) {
            throw new NotFoundException("No user with id " + id);
        }
        return user;
    }

    /**
     * This inserts a new <code>User</code> object.
     *
     * @param user The object to be added.
     * @return The object to be added.
     */
    @ApiMethod(name = "insertUser")
    public User insertUser(User user) throws ConflictException {
        if (user.getId()!=null) {
            if (findRecord(user.getId()) != null) {
                throw new ConflictException("Object already exists");
            }
        }
       ofy().save().entity(user).now();
       return user;
    }



    //Private method to retrieve a <code>Quote</code> record
    private User findRecord(String id) {
        return ofy().load().group(Consensus.class).type(User.class).id(id).now();
//or return ofy().load().type(Quote.class).filter("id",id).first.now();
    }

}