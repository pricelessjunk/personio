package com.personio.demo.domain.usecases;

import com.personio.demo.domain.Node;
import com.personio.demo.exceptions.CyclicStructureException;
import com.personio.demo.exceptions.MultipleRootSupervisorException;

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

    public void verifyMultipleRoot(Map<String, Node> tracker) throws MultipleRootSupervisorException {
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

    public void verifyCyclicReference(Map<String, Node> tracker) throws CyclicStructureException {
        for(Node current : tracker.values()){
            if (checkChildInParent(current, current.getParent())) {
                throw new CyclicStructureException("A cycle has been detected for the person " + current.getName());
            }
        }
    }

    private boolean checkChildInParent(Node current, Node parent) {
        if (parent == null) {
            return false;
        } else if (parent.isNameSame(current.getName())) {
            return true;
        } else {
            return checkChildInParent(current, parent.getParent());
        }
    }



}
