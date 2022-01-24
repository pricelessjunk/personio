package com.personio.demo.domain.usecases.structure;

import com.personio.demo.domain.entities.Node;
import com.personio.demo.domain.exceptions.CyclicStructureException;
import com.personio.demo.domain.exceptions.MultipleRootSupervisorException;

import javax.enterprise.context.ApplicationScoped;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * This usecase is used to perform various verifications of the given data
 */
@ApplicationScoped
public class StructureVerificationUsecase {

    /**
     * Verifies if multiple roots are present in the hierarchical structure
     *
     * @param tracker the name tp {@link Node} map
     */
    public void verifyMultipleRoot(Map<String, Node> tracker) {
        List<String> roots = new ArrayList<>();

        tracker.forEach((key, value) -> {
            if (value.getParent() == null) {
                roots.add(value.getName());
            }
        });

        if (roots.size() > 1) {
            String message = "Multiple root supervisors found: " + roots.stream().collect(Collectors.joining(", "));
            throw new MultipleRootSupervisorException(message);
        }
    }

    /**
     * Verifies if a cycle is present in the hierarchical structure
     *
     * @param tracker the name tp {@link Node} map
     */
    public void verifyCyclicReference(Map<String, Node> tracker) {
        for (Node current : tracker.values()) {
            String path = checkNodeInParent(current);
            if (!"".equals(path)) {
                throw new CyclicStructureException("A cycle has been detected: " + path);
            }
        }
    }

    /**
     * This method checks if a node is also present in any of its parent,
     * It is used to detect cycles.
     *
     * @param current the current node
     * @return the path if a cycle is present, else an empty string
     */
    private String checkNodeInParent(Node current) {
        Node parent = current.getParent();

        if (parent == null) {
            return "";
        }

        List<String> path = new ArrayList<>();
        path.add(current.getName());
        boolean cycle = true;

        while (!current.isNameSame(parent.getName())) {
            path.add(parent.getName());
            parent = parent.getParent();

            if (parent == null) {
                cycle = false;
                break;
            }
        }

        return cycle ? path.stream().collect(Collectors.joining(" -> ")) : "";
    }

}
