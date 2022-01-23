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
            if(value.getParent() == null) {
                roots.add(value.getName());
            }
        });

        if(roots.size() > 1) {
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
        for(Node current : tracker.values()){
            if (checkNodeInParent(current, current.getParent())) {
                throw new CyclicStructureException("A cycle has been detected for the person " + current.getName());
            }
        }
    }

    /**
     * This method recursively checks if a node is also present in any of its parent,
     * It is used to detect cycles.
     *
     * @param current the current node
     * @param parent the parent node
     * @return true if it is present, else false
     */
    private boolean checkNodeInParent(Node current, Node parent) {
        if (parent == null) {
            return false;
        } else if (parent.isNameSame(current.getName())) {
            return true;
        } else {
            return checkNodeInParent(current, parent.getParent());
        }
    }



}
