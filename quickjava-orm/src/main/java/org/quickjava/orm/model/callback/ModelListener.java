package org.quickjava.orm.model.callback;

import org.quickjava.orm.model.Model;

public interface ModelListener {

    void insert(Model model);

    void delete(Model model);

    void update(Model model);

    void select(Model model);
}
