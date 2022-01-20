package com.personio.demo.domain.usecases;

import com.personio.demo.domain.entities.CyclicPair;
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
            if( value.getParent() == null) {
                roots.add(value.getName());
            }
        });

        if(roots.size() > 1) {
            String message = "Multiple root supervisors found: " + roots.stream().collect(Collectors.joining(","));
            throw new MultipleRootSupervisorException(message);
        }
    }

    public void verifyCyclicReference(Map<String, Node> tracker) throws CyclicStructureException {
        List<CyclicPair> cyclicPairs = new ArrayList<>();

        tracker.forEach((key, value) -> {
            for(Node child : value.getChildren()) {
                if (checkChildInParent(child.getName(), child.getParent())) {
                    cyclicPairs.add(new CyclicPair(child.getParent().getName(), child.getName()));
                }
            }
        });

        if (!cyclicPairs.isEmpty()) {
            String message = cyclicPairs
                    .stream().map(CyclicPair::toString)
                    .collect(Collectors.joining("\n"));
            throw new CyclicStructureException(message);
        }
    }

    private boolean checkChildInParent(String childName, Node parent) {
        if (parent == null) {
            return false;
        } else if (parent.isNameSame(childName)) {
            return true;
        } else {
            return checkChildInParent(childName, parent.getParent());
        }
    }



}
