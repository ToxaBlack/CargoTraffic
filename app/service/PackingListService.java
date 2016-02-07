package service;

import play.Logger;
import repository.PackingListRepository;

import javax.inject.Inject;

/**
 * Created by Olga on 07.02.2016.
 */
public class PackingListService {
    private static final Logger.ALogger LOGGER = Logger.of(PackingListService.class);
    @Inject
    private PackingListRepository repository;

}
