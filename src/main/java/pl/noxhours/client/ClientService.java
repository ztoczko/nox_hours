package pl.noxhours.client;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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

    public List<Client> findAll() {
        return clientRepository.findAll();
    }

    public Page<Client> findAll(Pageable pageable, boolean all) {
        return all ? clientRepository.findAllBy(pageable) : clientRepository.findAllByClosed(pageable, false);
    }

    public Page<Client> findAllSearch(Pageable pageable, String search, boolean all) {
        return all ? clientRepository.findAllByNameContains(pageable, search) : clientRepository.findAllByClosedAndNameContains(pageable, false, search);
    }
}
