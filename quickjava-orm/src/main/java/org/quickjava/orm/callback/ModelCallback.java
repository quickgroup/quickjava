package org.quickjava.orm.callback;

import org.quickjava.orm.Model;

public interface ModelCallback {

    void insert(Model model);

    void delete(Model model);

    void update(Model model);

    void select(Model model);
}
