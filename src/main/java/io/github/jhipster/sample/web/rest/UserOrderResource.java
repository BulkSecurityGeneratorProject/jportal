package io.github.jhipster.sample.web.rest;

import com.codahale.metrics.annotation.Timed;
import io.github.jhipster.sample.domain.UserOrder;
import io.github.jhipster.sample.service.UserOrderService;
import io.github.jhipster.sample.web.rest.util.HeaderUtil;
import io.github.jhipster.sample.web.rest.util.PaginationUtil;

import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing UserOrder.
 */
@RestController
@RequestMapping("/api")
public class UserOrderResource {

    private final Logger log = LoggerFactory.getLogger(UserOrderResource.class);
        
    @Inject
    private UserOrderService userOrderService;

    /**
     * POST  /user-orders : Create a new userOrder.
     *
     * @param userOrder the userOrder to create
     * @return the ResponseEntity with status 201 (Created) and with body the new userOrder, or with status 400 (Bad Request) if the userOrder has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/user-orders")
    @Timed
    public ResponseEntity<UserOrder> createUserOrder(@Valid @RequestBody UserOrder userOrder) throws URISyntaxException {
        log.debug("REST request to save UserOrder : {}", userOrder);
        if (userOrder.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("userOrder", "idexists", "A new userOrder cannot already have an ID")).body(null);
        }
        UserOrder result = userOrderService.save(userOrder);
        return ResponseEntity.created(new URI("/api/user-orders/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("userOrder", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /user-orders : Updates an existing userOrder.
     *
     * @param userOrder the userOrder to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated userOrder,
     * or with status 400 (Bad Request) if the userOrder is not valid,
     * or with status 500 (Internal Server Error) if the userOrder couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/user-orders")
    @Timed
    public ResponseEntity<UserOrder> updateUserOrder(@Valid @RequestBody UserOrder userOrder) throws URISyntaxException {
        log.debug("REST request to update UserOrder : {}", userOrder);
        if (userOrder.getId() == null) {
            return createUserOrder(userOrder);
        }
        UserOrder result = userOrderService.save(userOrder);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("userOrder", userOrder.getId().toString()))
            .body(result);
    }

    /**
     * GET  /user-orders : get all the userOrders.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of userOrders in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @GetMapping("/user-orders")
    @Timed
    public ResponseEntity<List<UserOrder>> getAllUserOrders(@ApiParam Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of UserOrders");
        Page<UserOrder> page = userOrderService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/user-orders");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /user-orders/:id : get the "id" userOrder.
     *
     * @param id the id of the userOrder to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the userOrder, or with status 404 (Not Found)
     */
    @GetMapping("/user-orders/{id}")
    @Timed
    public ResponseEntity<UserOrder> getUserOrder(@PathVariable Long id) {
        log.debug("REST request to get UserOrder : {}", id);
        UserOrder userOrder = userOrderService.findOne(id);
        return Optional.ofNullable(userOrder)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /user-orders/:id : delete the "id" userOrder.
     *
     * @param id the id of the userOrder to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/user-orders/{id}")
    @Timed
    public ResponseEntity<Void> deleteUserOrder(@PathVariable Long id) {
        log.debug("REST request to delete UserOrder : {}", id);
        userOrderService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("userOrder", id.toString())).build();
    }

}
