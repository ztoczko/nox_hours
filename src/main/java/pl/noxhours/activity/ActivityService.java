package pl.noxhours.activity;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.noxhours.client.Client;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ActivityService {

    private final ActivityRepository activityRepository;

    public void create(Activity activity) {
        activityRepository.save(activity);
    }

    public Activity read(Long id) {
        return activityRepository.findById(id).orElse(null);
    }

    public void update(Activity activity) {
        activityRepository.save(activity);
    }

    public void delete(Activity activity) {
        activityRepository.delete(activity);
    }

    public List<Activity> findAll(Client client) {
        return activityRepository.findAllByClient(client);
    }

    public List<Activity> findRecent() {
        return activityRepository.findFirst10ByOrderByCreatedDesc();
    }

}
