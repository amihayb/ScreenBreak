package com.screenbreak.common;

import com.screenbreak.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import androidx.annotation.AnimatorRes;

public class FragmentAnimationCycle {

    private static volatile FragmentAnimationCycle instance;

    public static FragmentAnimationCycle getInstance() {
        if (instance == null) {
            synchronized (FragmentAnimationCycle.class) {
                if (instance == null) {
                    instance = new FragmentAnimationCycle();
                }
            }
        }
        return instance;
    }


    private List<AnimationTuple> m_animationTupleList;

    private int m_lastIndex;

    private FragmentAnimationCycle() {
        m_animationTupleList = new ArrayList<>();
        m_animationTupleList.add(new AnimationTuple(R.anim.push_down_in, R.anim.push_down_out));
        m_animationTupleList.add(new AnimationTuple(R.anim.push_left_in, R.anim.push_left_out));
        m_animationTupleList.add(new AnimationTuple(R.anim.push_up_in, R.anim.push_up_out));
        m_animationTupleList.add(new AnimationTuple(R.anim.push_right_in, R.anim.push_right_out));

        m_lastIndex = 0;
    }

    public AnimationTuple getNext() {
        AnimationTuple animationTuple = m_animationTupleList.get(m_lastIndex);
        m_lastIndex = m_lastIndex + 1 >= m_animationTupleList.size() ? 0 : m_lastIndex + 1;
        return animationTuple;
    }

    public AnimationTuple getRandom() {
        return m_animationTupleList.get(new Random().nextInt(m_animationTupleList.size()));
    }

    public class AnimationTuple {
        @AnimatorRes
        int m_goInAnim, m_goOutAnim;

        private AnimationTuple(int goInAnim, int goOutAnim) {
            m_goInAnim = goInAnim;
            m_goOutAnim = goOutAnim;
        }

        public int getGoInAnim() {
            return m_goInAnim;
        }

        public int getGoOutAnim() {
            return m_goOutAnim;
        }
    }
}
