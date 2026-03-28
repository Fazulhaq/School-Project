import React from 'react';
import { Translate } from 'react-jhipster';

import MenuItem from 'app/shared/layout/menus/menu-item';

const EntitiesMenu = () => {
  return (
    <>
      {/* prettier-ignore */}
      <MenuItem icon="asterisk" to="/student">
        <Translate contentKey="global.menu.entities.student" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/parent">
        <Translate contentKey="global.menu.entities.parent" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/teacher">
        <Translate contentKey="global.menu.entities.teacher" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/student-class">
        <Translate contentKey="global.menu.entities.studentClass" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/section">
        <Translate contentKey="global.menu.entities.section" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/subject">
        <Translate contentKey="global.menu.entities.subject" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/teacher-subject">
        <Translate contentKey="global.menu.entities.teacherSubject" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/enrollment">
        <Translate contentKey="global.menu.entities.enrollment" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/attendance">
        <Translate contentKey="global.menu.entities.attendance" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/exam">
        <Translate contentKey="global.menu.entities.exam" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/exam-subject">
        <Translate contentKey="global.menu.entities.examSubject" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/student-result">
        <Translate contentKey="global.menu.entities.studentResult" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/fee-structure">
        <Translate contentKey="global.menu.entities.feeStructure" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/payment">
        <Translate contentKey="global.menu.entities.payment" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/timetable">
        <Translate contentKey="global.menu.entities.timetable" />
      </MenuItem>
      {/* jhipster-needle-add-entity-to-menu - JHipster will add entities to the menu here */}
    </>
  );
};

export default EntitiesMenu;
