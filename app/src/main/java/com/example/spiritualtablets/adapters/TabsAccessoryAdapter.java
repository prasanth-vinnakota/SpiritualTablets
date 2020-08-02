package com.example.spiritualtablets.adapters;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.spiritualtablets.fragments.ChatsFragment;
import com.example.spiritualtablets.fragments.ContactsFragment;
import com.example.spiritualtablets.fragments.GroupsFragment;
import com.example.spiritualtablets.fragments.RequestFragment;

public class TabsAccessoryAdapter extends FragmentPagerAdapter {
    public TabsAccessoryAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }


    @Override
    public Fragment getItem(int position) {

        switch (position){

            case 0:
                return new ChatsFragment();

            case 1:
                return new GroupsFragment();

            case 2:
                return new ContactsFragment();

            case 3:
                return new RequestFragment();

            default:
                return null;
        }
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {

        switch (position) {

            case 0:
                return "Chats";

            case 1:
                return "Groups";

            case 2:
                return "Contacts";

            case 3:
                return "Requests";

            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 4;
    }
}
