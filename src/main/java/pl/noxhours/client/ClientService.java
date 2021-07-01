package pl.noxhours.client;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ClientService {

    private final ClientRepository clientRepository;

    public void create(Client client) {
        clientRepository.save(client);
    }

    public Client read(Long id) {
        return clientRepository.findById(id).orElse(null);
    }

    public void update(Client client) {
        clientRepository.save(client);
    }

    public void delete(Client client) {
        clientRepository.delete(client);
    }

    public List<Client> getAll() {
        return clientRepository.findAll();
    }

}
